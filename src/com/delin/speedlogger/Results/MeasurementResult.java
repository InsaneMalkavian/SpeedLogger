package com.delin.speedlogger.Results;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;

public enum MeasurementResult {
    /**
     * This class stores location list and provides access to it for any activity.
     */
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
