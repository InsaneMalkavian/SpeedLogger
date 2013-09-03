package com.delin.speedlogger.Results;

public class StoredRecord {
	public class CarInfo {
		String 	mBrand;
		String 	mModel;
		String 	mModelIndex;
		String 	mGearbox; // enum: man, hydro, cv, robot, dual clutch robot
		long 	mHorsePower;
		long 	mTorque;
		// stop me please :)
		
		public CarInfo(){
			
		}
		
		//========SETTERS==========================
		public void SetBrand(String brand) {
			mBrand = brand;
		}
		
		public void SetModel(String model) {
			mModel = model;
		}
		
		public void SetModelIndex(String modelIndex) {
			mModelIndex = modelIndex;
		}
		
		public void SetGearbox(String gearbox) {
			mGearbox = gearbox;
		}
		
		public void SetHorsePower(long horsePower) {
			mHorsePower = horsePower;
		}
		
		public void SetTorque(long torque) {
			mTorque = torque;
		}
		
		//========GETTERS==========================
		public String GetBrand() {
			return mBrand;
		}
		
		public String GetModel() {
			return mModel;
		}
		
		public String GetModelIndex() {
			return mModelIndex;
		}
		
		public String GetGearbox() {
			return mGearbox;
		}
		
		public long GetHorsePower() {
			return mHorsePower;
		}
		
		public long GetTorque() {
			return mTorque;
		}
		
		@Override
		public String toString() {
			return new String
					(
						"mBrand = " 		+ mBrand		+ "; " +
						"mModel = " 		+ mModel		+ "; " +
						"mModelIndex = "	+ mModelIndex	+ "; " +
						"mGearbox = "		+ mGearbox		+ "; " +
						"mHorsePower = " 	+ mHorsePower	+ "; " +
						"mTorque = " 		+ mTorque		+ "; "
					);
		}
	}
	
	private long mStartTime = 0;
	private long mTotalTime = 0;
	private float mMaxSpeed = 0;
	private float mTotalDistance = 0;
	
	private float[] mTimes; // not in use yet
	
	private CarInfo mCar;

	public StoredRecord() {
		SetCar(new CarInfo());
	}
	
	public StoredRecord(SessionResult result) {
		SetCar				(new CarInfo());
		SetStartTime		(result.GetStartTime());
		SetTotalTime		(result.GetTotalTime());
		SetMaxSpeed			(result.GetMaxSpeed());
		SetTotalDistance	(result.GetTotalDistance());
	}
	
	/**
	 * Returns top speed
	 *
	 * @return Speed, in m/s
	 */
    public float GetMaxSpeed() {
    	return mMaxSpeed;
    }
    
    /**
	 * Returns distance on which top speed has been achieved
	 *
	 * @return Distance, in meters
	 */
    public float GetTotalDistance() {
    	return mTotalDistance;
    }
    
	/**
	 * Returns time taken on achievement top speed
	 *
	 * @return Time, in milliseconds, since StartTime!
	 */
    public long GetTotalTime() {
    	return mTotalTime;
    }
    
	/**
	 * Returns time at which session has been started
	 *
	 * @return Time at which session has been started, in milliseconds since 01.01.1970 UTC
	 */
    public long GetStartTime() {
    	return mStartTime; 
    }
    
    /**
	 * Returns information about car on which session has been done
	 *
	 * @return Information about car on which session has been done
	 */
    public CarInfo GetCar() {
    	return mCar; 
    }
    
    public void SetStartTime(long startTime) {
		mStartTime = startTime;
	}

	public void SetMaxSpeed(float maxSpeed) {
		mMaxSpeed = maxSpeed;
	}

	public void SetTotalDistance(float totalDistance) {
		mTotalDistance = totalDistance;
	}

	public void SetTotalTime(long totalTime) {
		mTotalTime = totalTime;
	}
	
	public void SetCar(CarInfo car) {
    	mCar = car; 
    }
	
	@Override
	public String toString() {
		return new String
				(
					"mStartTime = " 	+ mStartTime 	  + "; " +
					"mTotalTime = " 	+ mTotalTime 	  + "; " +
					"mMaxSpeed = " 		+ mMaxSpeed  	  + "; " +
					"mTotalDistance = " + mTotalDistance  + "; " +
					"mCar = (" 			+ mCar.toString() + ") "
				);
	}
	
}
