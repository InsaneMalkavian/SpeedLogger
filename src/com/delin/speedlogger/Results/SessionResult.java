package com.delin.speedlogger.Results;

import java.util.ArrayList;
import java.util.List;

import com.delin.speedlogger.Math.Geometry;
import com.delin.speedlogger.Serialization.GPXSerializer;

import android.location.Location;

public class SessionResult {
	public long mStartTime  = 0;
	public float mMaxSpeed  = 0;
	public float mDistance  = 0; // meters
	public long mDuration   = 0;
	private List<Location> mLocList;
	
	public SessionResult() {
	}
	
	public SessionResult(List<Location> locList) {
		if(locList == null || locList.size() < 2) return;
		mLocList = new ArrayList<Location>(locList);
		Location origin = mLocList.get(0);
		Location dest = mLocList.get(mLocList.size()-1);
		
		mStartTime = mLocList.get(0).getTime();
		for (int i=1; i<mLocList.size(); i++){
			if (mMaxSpeed < mLocList.get(i).getSpeed()){
				mMaxSpeed = mLocList.get(i).getSpeed();
			}
		}
		mDuration = dest.getTime() - mStartTime;
		// TODO: One of them is VERY wrong
		//mDistance = origin.distanceTo(dest);   // returns 87.4m
		mDistance = Geometry.DistBetweenLocs(origin,dest,false); // returns 156m
	}

	public List<Location> GetLocations() {
		if(mLocList == null) LoadGPX();
		return mLocList;
	}
	
	public void SaveGPX() {
		if(mLocList == null || mLocList.size() < 2) return;
		String filename = Long.toString(mStartTime) + ".gpx";
		GPXSerializer gpxLog = new GPXSerializer(filename, true);
		gpxLog.SaveAllFixes(mLocList);
		gpxLog.Stop();
	}
	
	private void LoadGPX() {
		String filename = Long.toString(mStartTime) + ".gpx";
		GPXSerializer gpxLog = new GPXSerializer(filename, false);
		mLocList = gpxLog.GetAllFixes();
		// HINT: we can check gpx data here to prevent hacks
	}
}
