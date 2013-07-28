package com.delin.speedlogger.GPS;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class RealGPSProvider extends GPSProvider implements LocationListener {
	LocationManager mLocationManager = null;
	
	public RealGPSProvider(Context context, LocationListener listener) {
		super(context, listener);
		mLocationManager = (LocationManager) mContext.getSystemService(android.content.Context.LOCATION_SERVICE);
	}
	
	public void Start() {
		// Register the listener with the Location Manager to receive location updates
		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this); // TODO: remove
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	}
	
	public void Stop() {
		// Remove the listener you previously added
		mLocationManager.removeUpdates(this);
	}
	
	@Override
	public void onLocationChanged(Location arg0) {
		mListener.onLocationChanged(arg0);
	}

	@Override
	public void onProviderDisabled(String arg0) {
		mListener.onProviderDisabled(arg0);
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		mListener.onProviderEnabled(arg0);
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		mListener.onStatusChanged(arg0, arg1, arg2);
	}

}
