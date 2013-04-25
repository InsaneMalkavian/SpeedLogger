package com.delin.speedlogger.GPS;

import com.delin.speedlogger.Serialization.GPXSerializer;
import com.delin.speedlogger.Utils.Timer;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;

public class FileGPSProvider extends GPSProvider {
	
	GPXSerializer mGPXReader = null;
	Timer mTimer = null;

	public FileGPSProvider(Context context, LocationListener listener) {
		super(context, listener);
		mGPXReader = new GPXSerializer("gps2read.gpx", false);
		mTimer = new Timer(new Runnable() {
			@Override
		     public void run() {
				Location loc = mGPXReader.GetFix();
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