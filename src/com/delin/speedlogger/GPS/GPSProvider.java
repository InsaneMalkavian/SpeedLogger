package com.delin.speedlogger.GPS;

import android.content.Context;
import android.location.LocationListener;

public abstract class GPSProvider {
	LocationListener mListener;
	Context mContext;	
	
	public GPSProvider(Context context, LocationListener listener) {
		mContext=context;
		mListener = listener;
	}
	
	abstract public void Start();
	
	abstract public void Stop();

}
