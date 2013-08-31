package com.delin.speedlogger.TrackingSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.delin.speedlogger.R;
import com.delin.speedlogger.GPS.*;
import com.delin.speedlogger.Serialization.GPXSerializer;

import android.location.*;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.content.Context;
import android.content.SharedPreferences;

public class TrackingSession implements LocationListener {
	enum TrackingState { WARMUP, READY, TRACKING, ERROR, DONE, IDLE }
	public enum WarmupState {NO_GPS, WAITING_FIX, BAD_ACCURACY, HIGH_SPEED }
	
	final static String UNCERT_LOC = "uncertainty";
	final static int MAX_LOC_COUNT = 300; // 300/60 fixes per minute = 5 min
	final static float HOR_ACCURACY = 25.f; // horizontal accuracy, in meters
	final static float SPEED_THRESHOLD = 1.75f; // speed threshold to detect start, in m/s
	final static float MAX_SPEED_MULTIPLY_THRESHOLD = 0.9f; // stop tracking if speed falls more that 90% of current max
	
	//--- session measured parameters
	float mMaxSpeed = 0.f;
	List<Location> mLocList = new ArrayList<Location>();
	
	Location mBaseLocation = null; // last location
	Location mReadyLoc = null;
	GPSProvider mGpsProvider = null;	
	boolean mGPSEnabled = false; // gps receiver status
	boolean mWriteGPX = false;
	GPXSerializer mGpxLog = null;
	TrackingState mState = TrackingState.IDLE;
	WarmupState mWarmupState = WarmupState.WAITING_FIX;
	boolean mStartLocked = false;
	Vector<TrackingSessionListener> mListeners = new Vector<TrackingSessionListener>();
	Context mContext;
	long mOffsetTime = 0; // device time - satellite time, in ms
	
	public TrackingSession(Context context) {
		mContext = context;
		String prefsName = context.getString(R.string.DevPrefs);
		SharedPreferences devPrefs = context.getSharedPreferences(prefsName,Context.MODE_PRIVATE);
		
		mWriteGPX = devPrefs.getBoolean(context.getString(R.string.SaveTrackingSession), false);
		
		String provider = context.getString(R.string.RealGPS);
		provider = devPrefs.getString(context.getString(R.string.ProviderGPS),provider);
		if(provider.equals(context.getString(R.string.RealGPS))){ 
			mGpsProvider = new RealGPSProvider(context, this);
		}
		else{
			mGpsProvider = new FileGPSProvider(context, this);
		}
		
		StartService();
	}
	
	public void AddListener(TrackingSessionListener newListener) {
		mListeners.add(newListener);
		// notify about current state!
		switch (mState)
		{
		case WARMUP:
			newListener.onSessionWarmingUp(mWarmupState);
			break;
		case READY:
			newListener.onSessionReady();
			break;
		case TRACKING:
			newListener.onSessionStart(mReadyLoc.getTime());
			break;
		case DONE:
		case ERROR:
		case IDLE:
			break;
		}
	}
	
	public void RemoveListener(TrackingSessionListener obsoleteListener) {
		mListeners.remove(obsoleteListener);
	}
	
	public Location GetLastLocation() {
		return mBaseLocation;
	}
	
	/**
	 * Returns location where the session has been started.
	 * 
	 * This location may have recalculated position, time instead of provided via gps/glns.
	 * The position may be calculated by using several previous fixes,
	 * and time of fix may be adjusted by accelerometer data.
	 * <p>
	 * This location always comes first in trackingsession's location list.
	 * 
	 * @return Location at which session has been started
	 */
	public Location GetReadyLocation() {
		return mReadyLoc;
	}
	
	public void StartService() {
		if (mState==TrackingState.IDLE) { // start only from IDLE state
			ResetSessionValues();
			mGPSEnabled = true; // in case gps is off we'll receive onDisabled and disable it
			mBaseLocation = new Location(UNCERT_LOC);
			mReadyLoc = new Location(UNCERT_LOC);
			mState = TrackingState.WARMUP;
			if (mWriteGPX) {
				mGpxLog = new GPXSerializer();
			}
			for (TrackingSessionListener listener : mListeners)
			{
				listener.onSessionWarmingUp(mWarmupState);
			}
			mGpsProvider.Start();
		}
	}
	
	public void StopService() {
		if (mState!=TrackingState.IDLE) { // stop only active
			mGpsProvider.Stop();

			if (mWriteGPX) {
				mGpxLog.Stop();
				mGpxLog = null;
			}
			if (mState==TrackingState.TRACKING) { // save active session
				SessionDone();
			}
			mState = TrackingState.IDLE;
			for (TrackingSessionListener listener : mListeners)
			{
				listener.onSessionStopped();
			}
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// we've got a new fix
		mBaseLocation = location; // update last known location
		for (TrackingSessionListener listener : mListeners)
		{ // notify listeners
			listener.onSessionLocationUpdate(location);
		}
		if (mWriteGPX) { // save fix to gpx track
			mGpxLog.AddFix(location);
		}
		if (mGPSEnabled == false) return; // gps is off, location has come from cell/wifi - so just skip it
		switch (mState)
		{ // update logic
		case WARMUP:
			Log.i("TrackingSession", "Warming up...");
			// here we check for problems to solve before we can start
			if (location.hasAccuracy() && location.getAccuracy()>HOR_ACCURACY)
			{ 
				if (mWarmupState != WarmupState.BAD_ACCURACY) {
					SetWarmingState(WarmupState.BAD_ACCURACY);
				}
				
			}
			else if (location.hasSpeed() && location.getSpeed() > 0)
			{
				if (mWarmupState != WarmupState.HIGH_SPEED) {
					SetWarmingState(WarmupState.HIGH_SPEED);
				}
			}
			else{
				// there are no problems, we're ready
				mState = TrackingState.READY;
				mReadyLoc=location;
				for (TrackingSessionListener listener : mListeners)
				{ //onSessionReady()
					listener.onSessionReady();
				}
			}
			break;
		case READY:
			Log.i("TrackingSession", "We are ready");
			// here we should determine when to start or get back to WARMUP in case of bad fix
			if (location.hasAccuracy() && location.getAccuracy()>HOR_ACCURACY)
			{ // in case of bad fix stop tracking
				mState = TrackingState.WARMUP;
				SetWarmingState(WarmupState.BAD_ACCURACY);
			}
			// here is some logic to make it start or get back to warming if we moved
			else if (location.hasSpeed() && location.getSpeed()>SPEED_THRESHOLD) {
				if (mStartLocked == false) { // autostart, let's go
					mState = TrackingState.TRACKING;
					mOffsetTime = System.currentTimeMillis() - location.getTime();
					for (TrackingSessionListener listener : mListeners)
					{
						listener.onSessionStart(mReadyLoc.getTime());
					}
					// push mReadyLoc to list
					mLocList.add(mReadyLoc);
					// push current loc to list
					mLocList.add(location);
				}
				else { // autostart blocked, get back to warming up
					mState = TrackingState.WARMUP;
					SetWarmingState(WarmupState.HIGH_SPEED);
				}
			}
			else {
				// if not start - just resave prestart loc
				mReadyLoc = location;
				mReadyLoc.setTime(System.currentTimeMillis());
				// we can calculate origin by taking average fix, not last one
			}
			break;
		case TRACKING:
			Log.i("TrackingSession", "Tracking, go-go-go");
			/*if (false)//location.getAccuracy()>HOR_ACCURACY)
			{ // in case of overflow or bad fix stop trackingSessionDone
				SessionDone();
			}
			else */if (location.hasSpeed()== false || location.getSpeed()<mMaxSpeed*MAX_SPEED_MULTIPLY_THRESHOLD
					|| mLocList.size()>=MAX_LOC_COUNT)
			{ // good stop, there is no difference with bad stop
				SessionDone();
			}
			else {
				// and just save loc if all goes normal
				mLocList.add(location);
				if (mMaxSpeed<location.getSpeed()) mMaxSpeed=location.getSpeed();
			}
			break;
		case ERROR:
		case IDLE:
		case DONE:
			break;
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		mGPSEnabled = false;
		Toast toast = Toast.makeText(mContext, mContext.getString(R.string.EnableGPS), Toast.LENGTH_LONG);
		toast.show();
		// current warming - goto warming, nothing to change
		// ready -> warming
		// tracking -> done and close
		// so we can be only in warmingup state after provider has been disabled
		switch (mState) {
		case WARMUP:
		case READY:
			mState = TrackingState.WARMUP;
			SetWarmingState(WarmupState.NO_GPS);
			break;
		case TRACKING:
			SessionDone();
			break;
		case ERROR:
		case IDLE:
		case DONE:
			break;
		}
	}

	@Override
	public void onProviderEnabled(String provider) {
		SetWarmingState(WarmupState.WAITING_FIX);
		mGPSEnabled = true;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
	
	public long GetOffsetTime() {
		return mOffsetTime;
	}
	
	/**
	 * Lock/unlock automatic start 
	 * 
	 * Tracking session won't start automatically if it's blocked. Note that the 
	 * session still will be receiving gps fixes and staying in warming/ready state.
	 * When block is disabled session can start in normal order (in case accuracy and
	 * speed satisfy requirements) 
	 * 
	 * @param state true to lock session, false to release
	 * 
	 */
	public void SetStartLocked(boolean state) {
		mStartLocked = state;
	}
	
	private void SessionDone() {
		Log.i("TrackingSession", "Session done");
		// here is some logic to make it stop the good way
		mState = TrackingState.DONE;
		for (TrackingSessionListener listener : mListeners)
		{ // stop tracking
			listener.onSessionFinished(mLocList);
		}
	}
	
	private void ResetSessionValues() {
		mMaxSpeed = 0.f;
		mLocList.clear();
	}
	
	private void SetWarmingState(WarmupState state) {
		mWarmupState = state;
		for (TrackingSessionListener listener : mListeners) {
			listener.onSessionWarmingUp(mWarmupState);
		}
	}
}
