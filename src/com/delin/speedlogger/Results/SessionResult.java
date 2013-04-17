package com.delin.speedlogger.Results;

import java.util.ArrayList;
import java.util.List;

import com.delin.speedlogger.Math.Geometry;
import com.delin.speedlogger.Serialization.GPXSerializer;

import android.location.Location;

public class SessionResult {
	private long mStartTime  = 0;
	private float mMaxSpeed  = 0;
	private float mDistance  = 0; // meters
	private long mDuration   = 0;
	private String mFilename; // gpx file
	private List<Location> mLocList;
	
	public SessionResult() {
	}
	
	public SessionResult(List<Location> locList) {
		if(locList == null || locList.size() < 2) return;
		mLocList = new ArrayList<Location>(locList);
		Location origin = mLocList.get(0);
		Location dest = mLocList.get(mLocList.size()-1);
		
		setStartTime(mLocList.get(0).getTime());
		for (int i=1; i<mLocList.size(); i++){
			if (mMaxSpeed < mLocList.get(i).getSpeed()){
				setMaxSpeed(mLocList.get(i).getSpeed());
			}
		}
		setDuration(dest.getTime() - mStartTime);
		// TODO: One of them is VERY wrong
		//mDistance = origin.distanceTo(dest); // returns 87.4m
		setDistance(Geometry.DistBetweenLocs(origin,dest,false)); // returns 156m
	}
	
	public long getStartTime() {
		return mStartTime;
	}

	public void setStartTime(long startTime) {
		mStartTime = startTime;
		mFilename = Long.toString(mStartTime) + ".gpx";
	}
	
	public float getMaxSpeed() {
		return mMaxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		mMaxSpeed = maxSpeed;
	}
	
	public float getDistance() {
		return mDistance;
	}

	public void setDistance(float distance) {
		mDistance = distance;
	}

	public long getDuration() {
		return mDuration;
	}

	public void setDuration(long duration) {
		mDuration = duration;
	}

	public List<Location> getLocations() {
		if(mLocList == null) LoadGPX();
		return mLocList;
	}
	
	public void SaveGPX() {
		if(mLocList == null || mLocList.size() < 2) return;
		GPXSerializer gpxLog = new GPXSerializer(mFilename, true);
		gpxLog.SaveAllFixes(mLocList);
		gpxLog.Stop();
	}
	
	private void LoadGPX() {
		GPXSerializer gpxLog = new GPXSerializer(mFilename, false);
		mLocList = gpxLog.GetAllFixes();
		// HINT: we can check gpx data here to prevent hacks
	}
	
	public void DeleteGPX() {
		GPXSerializer.DeleteGPX(mFilename);
	}
}
