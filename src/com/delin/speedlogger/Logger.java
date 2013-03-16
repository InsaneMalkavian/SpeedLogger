package com.delin.speedlogger;

import android.location.Location;

public class Logger {
	public static String LocToStr(Location loc) {
		return  "Latitude: " + Double.toString(loc.getLatitude()) + 
				"\nLongitude: " + Double.toString(loc.getLongitude()) +
    			"\nProvider: " + loc.getProvider() + 
    			"\nAccuracy (m): " + loc.getAccuracy() + 
    			"\nSpeed (kph): " + loc.getSpeed()*3.6 +
    			"\nAltitude (m): " + loc.getAltitude() + 
    			"\nBearing: " + loc.getBearing() + 
    			"\nTime: " + loc.getTime();
	}
}