package com.delin.speedlogger;

import java.util.ArrayList;
import java.util.List;
import android.location.Location;

public class SessionResult {
	long mStartTime  = 0;
	float mMaxSpeed  = 0;
	float mDistance  = 0; // meters
	long mDuration   = 0;
	private List<Location> mLocList;
	
	public SessionResult() {
	}
	
	public SessionResult(List<Location> locList) {
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
		if(mLocList != null) return mLocList;
		else{
			mLocList = new ArrayList<Location>();
			// TODO: load data here; use mStartTime as a key
			return mLocList;
		}
	}
}
