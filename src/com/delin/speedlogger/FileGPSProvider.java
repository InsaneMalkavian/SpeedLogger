package com.delin.speedlogger;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;

public class FileGPSProvider extends GPSProvider {
	
	GPXSerializer mGPXReader = null;
	Timer mTimer = null;

	public FileGPSProvider(Context context, LocationListener listener) {
		super(context, listener);
		mGPXReader = new GPXSerializer("/mnt/sdcard/default.gpx", false);
		mTimer = new Timer(new Runnable() {
			@Override
		     public void run() {
		    	//Location loc = mGPXReader.GetDummyFix();
				Location loc = mGPXReader.GetFix();
		    	mListener.onLocationChanged(loc);
		    };
		});
	}

	@Override
	public void Start() {
		// run timer
		//mTimer.start(1L * 1000);
		mTimer.start(10);
	}

	@Override
	public void Stop() {
		mTimer.stop();
	}

}