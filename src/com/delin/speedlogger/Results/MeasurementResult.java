package com.delin.speedlogger.Results;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;

/**
 * This class stores location list and provides access to it for any activity.
 */
public enum MeasurementResult {
    INSTANCE;
    private List<Location> locList = new ArrayList<Location>();

    public void addLocation(Location loc) {
        locList.add(loc);
    }
    
    public List<Location> getLocations() {
        return locList;
    }

    public void setLocations(List<Location> locs) { // do a full copy
        locList.clear();
        locList.addAll(locs);
    }
}
