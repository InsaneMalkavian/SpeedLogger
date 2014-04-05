package com.delin.speedlogger.GPS;

import com.delin.speedlogger.R;
import com.delin.speedlogger.Serialization.GPXSerializer;
import com.delin.speedlogger.Utils.Timer;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;

/** 
 * This class generates gps fixes from file. Time interval between
 * fixes is always 1 second, no matter how it's specified in gps
 * file!
 * */
public class FileGPSProvider extends GPSProvider {	
	GPXSerializer mGPXReader = null;
	Timer mTimer = null;

	public FileGPSProvider(Context context, LocationListener listener) {
		super(context, listener);
		// get gpx filename from devPrefs
		String devPrefsName = context.getString(R.string.DevPrefs);
		SharedPreferences devPrefs = context.getSharedPreferences(devPrefsName,Context.MODE_PRIVATE);
		String noFile = "noFile";
		String gpxFilename = devPrefs.getString(context.getString(R.string.FileWithGPS),noFile);
		// load gpx & launch provider
		mGPXReader = new GPXSerializer(gpxFilename, false);
		if (mGPXReader.isValid()) {
			mTimer = new Timer(new Runnable() {
				@Override
			     public void run() {
					Location loc = mGPXReader.GetFix();
					loc.setTime(System.currentTimeMillis());
			    	mListener.onLocationChanged(loc);
			    };
			});
		}
	}

	@Override
	public void Start() {
		// run timer
		if (mTimer!=null) mTimer.start(1L * 1000);
	}

	@Override
	public void Stop() {
		if (mTimer!=null) mTimer.stop();
	}

}