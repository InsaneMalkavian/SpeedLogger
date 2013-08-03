package com.delin.speedlogger.TrackingSession;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;

public class MeasurementResult {
	/**
	 * This class stores location list and provides access to it for any activities.
	 */
	private List<Location> mLocList = new ArrayList<Location>();
    private static final MeasurementResult instance = new MeasurementResult();    
    private MeasurementResult() {}
    public static MeasurementResult GetInstance() {
        return instance;
    }
    
    public void AddLocation(Location loc) {
    	mLocList.add(loc);
    }
    
    public List<Location> GetLocations() {
    	return mLocList;
    }

    public void SetLocations(List<Location> locs) { // do a full copy
    	mLocList.clear();
    	mLocList.addAll(locs);
    }
}
