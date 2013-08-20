package com.delin.speedlogger.Results;

public class StoredRecord {
	public class CarInfo {
		String mBrand;
		String mModel;
		String mModelIndex;
		String mGearbox; // enum: man, hydro, cv, robot, dual clutch robot
		long mHP;
		long mTorque;
		// stop me please :)
	}
	private long mStartTime = 0;
	private long mTotalTime = 0;
	private float mMaxSpeed = 0;
	private float mTotalDistance = 0;
	
	private float[] mTimes;
	
	private CarInfo mCar;	

	public StoredRecord() {
		
	}
}
