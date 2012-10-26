package com.delin.speedlogger;

import java.util.Vector;

import android.location.*;
import android.os.Bundle;
import android.content.Context;

public class TrackingSession implements LocationListener {
	enum TrackingState { WARMUP, READY, TRACKING, ERROR, IDLE }
	static int MAX_LOC_COUNT = 300; // 300/60 fixes per minute = 5 min
	static float HOR_ACCURACY = 20.f; // horizontal accuracy, in meters
	static float SPEED_THRESHOLD = 6.f; // speed threshold to detect start, in kmph
	
	//--- session measured parameters
	float mMaxSpeed = 0.f;
	
	Context mContext;
	LocationManager mLocationManager;
	boolean mEnabled;
	boolean mWriteGPX = true;
	GPXSerializer mGpxLog = null;
	TrackingState mState;
	Location[] mLocArray = new Location[MAX_LOC_COUNT]; // 5 min
	int mLocCount;
	Location mBaseLocation; // last location
	Location mReadyLoc;
	Vector<TrackingSessionListener> mListeners;
	
	public TrackingSession(Context Context) {
		mContext = Context;
		// Acquire a reference to the system Location Manager
		mLocationManager = (LocationManager) mContext.getSystemService(android.content.Context.LOCATION_SERVICE);
		mListeners = new Vector<TrackingSessionListener>();
		StopService(); // TODO: should we?
		StartService();
	}
	
	protected void finalize () {
		mListeners = null;
	}
	
	public void AddListener(TrackingSessionListener newListener) {
		mListeners.add(newListener);
		// notify about current state!
		switch (mState)
		{
		case WARMUP:
			newListener.onSessionWarmingUp();
			break;
		case READY:
			newListener.onSessionReady();
			break;
		case TRACKING:
			newListener.onSessionStart();
			break;
		default:
			break;
		}
	}
	
	public void RemoveListener(TrackingSessionListener obsoleteListener) {
		mListeners.remove(obsoleteListener);
	}
	
	public Location GetLastLocation() {
		return mBaseLocation;
	}
	
	public void StartService() {
		// TODO: check for mEnabled!
		// Register the listener with the Location Manager to receive location updates
		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this); // TODO: remove
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		mBaseLocation = new Location("uncertainty");
		mReadyLoc = new Location("uncertainty");
		mBaseLocation.reset();
		mReadyLoc.reset();
		mLocCount = 0;
		mState = TrackingState.WARMUP;
		if (mWriteGPX) {
			mGpxLog = new GPXSerializer("track.gpx");
		}
		for (TrackingSessionListener listener : mListeners)
		{
			listener.onSessionWarmingUp();
		}
	}
	
	public void StopService() {
		// Remove the listener you previously added
		mLocationManager.removeUpdates(this);
		
		mBaseLocation = null;
		mReadyLoc = null;
		if (mWriteGPX) {
			mGpxLog = null;
		}
		mState = TrackingState.IDLE;
		// TODO: stop all activities, notify listeners
	}
	
	public void StopTracking() {
		if (mState!=TrackingState.TRACKING)
			return;
		// proceed only if in tracking state
		onLocationChanged(new Location("uncertainty"));
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
		switch (mState)
		{ // update logic
		case WARMUP:
			// here we can only get a valid fix and become ready to start
			if (location.getAccuracy()<HOR_ACCURACY)			
			{ // in case of good fix notify about start availability
				mState = TrackingState.READY;	
				for (TrackingSessionListener listener : mListeners)
				{ //onSessionReady()
					listener.onSessionReady();
				}
			}
			break;
		case READY:
			// here we should determine when to start or get back to WARMUP in case of bad fix
			if (location.getAccuracy()>HOR_ACCURACY)
			{ // in case of bad fix stop tracking
				mState = TrackingState.WARMUP;
				for (TrackingSessionListener listener : mListeners)
				{ //onSessionWarmingUp()
					listener.onSessionWarmingUp();
				}
			}
			// here is some logic to make it start
			else if (location.getSpeed()>SPEED_THRESHOLD) {
				mState = TrackingState.TRACKING;
				for (TrackingSessionListener listener : mListeners)
				{
					listener.onSessionStart();
				}
				// push mReadyLoc to array
				mLocCount=0;
				mLocArray[mLocCount++]=mReadyLoc;
				// push current loc to array
				mLocArray[mLocCount++]=location;
				mMaxSpeed=location.getSpeed();
			}
			else {
				// if not start - just resave prestart loc
				mReadyLoc = location;
			}
			break;
		case TRACKING:
			if (location.getAccuracy()>HOR_ACCURACY)
			{ // in case of overflow or bad fix stop tracking
				mState = TrackingState.IDLE;
				for (TrackingSessionListener listener : mListeners)
				{ // stop tracking
					listener.onSessionError();
				}
			}
			else if (mLocCount>=MAX_LOC_COUNT || location.getSpeed()<mMaxSpeed) {
				// here is some logic to make it stop the good way
				mState = TrackingState.IDLE;
				for (TrackingSessionListener listener : mListeners)
				{ // stop tracking
					listener.onSessionFinished(mLocArray);
				}
			}
			else {
				// and just save loc if all goes normal
				mLocArray[mLocCount++]=location;
				mMaxSpeed=location.getSpeed();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		mEnabled = false;
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		mEnabled = true;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
