package com.delin.speedlogger.TrackingSession;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.delin.speedlogger.Serialization.GPXSerializer;

import android.location.Location;

public class MeasurementResult {

	private List<Location> mLocList = new ArrayList<Location>();
    private static final MeasurementResult instance = new MeasurementResult();    
    private MeasurementResult() {}
    public static MeasurementResult GetInstance() {
        return instance;
    }
    public void Clean() {
    	mLocList.clear();
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
    public void SaveToGPX(final String filename) {
    	GPXSerializer gpxSaver = new GPXSerializer(filename, true);
    	for (Iterator<Location> it = mLocList.iterator(); it.hasNext(); ) {
    		gpxSaver.AddFix(it.next());
    	}
    	gpxSaver.Stop();
    }
}
