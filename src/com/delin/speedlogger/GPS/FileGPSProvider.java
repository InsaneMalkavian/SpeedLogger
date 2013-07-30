package com.delin.speedlogger.GPS;

import com.delin.speedlogger.R;
import com.delin.speedlogger.Serialization.GPXSerializer;
import com.delin.speedlogger.Utils.Timer;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;

public class FileGPSProvider extends GPSProvider {
	static final String GPS_DIR_NAME = "FileGPS";
	static final String DEFAULT_GPX = GPS_DIR_NAME + "/" + "default.gpx";
	
	GPXSerializer mGPXReader = null;
	Timer mTimer = null;

	public FileGPSProvider(Context context, LocationListener listener) {
		super(context, listener);
		// get gpx filename from devPrefs
		String devPrefsName = context.getString(R.string.DevPrefs);
		SharedPreferences devPrefs = context.getSharedPreferences(devPrefsName,Context.MODE_PRIVATE);
		String gpxFilename = devPrefs.getString(context.getString(R.string.FileWithGPS),DEFAULT_GPX);
		// load gpx & launch provider
		mGPXReader = new GPXSerializer(gpxFilename, false);
		mTimer = new Timer(new Runnable() {
			@Override
		     public void run() {
				Location loc = mGPXReader.GetFix();
				loc.setTime(System.currentTimeMillis());
		    	mListener.onLocationChanged(loc);
		    };
		});
	}

	@Override
	public void Start() {
		// run timer
		mTimer.start(1L * 1000);
	}

	@Override
	public void Stop() {
		mTimer.stop();
	}

}