package com.delin.speedlogger;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;

public class FileGPSProvider extends GPSProvider {
	
	GPXSerializer mGPXReader = null;
	Timer mTimer = null;

	public FileGPSProvider(Context context, LocationListener listener) {
		super(context, listener);
		// TODO Auto-generated constructor stub
		mGPXReader = new GPXSerializer("/mnt/sdcard/default.gpx", false);
		mTimer = new Timer();
	}

	@Override
	public void Start() {
		// run timer
		mTimer.schedule(new TimerTask() {
		    @Override
		    public void run() {
		    	Location loc = mGPXReader.GetFix();
		    	mListener.onLocationChanged(loc);
		    };
		}, 0L, 1L * 1000);
	}

	@Override
	public void Stop() {
		mTimer.cancel();
	}

}
