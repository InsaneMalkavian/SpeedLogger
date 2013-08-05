package com.delin.speedlogger.Sensors;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.ArrayList;
import java.util.List;

import com.delin.speedlogger.R;
import com.delin.speedlogger.Serialization.AccelerationSerializer;

public class AccelerationProcessor implements SensorEventListener {
	public enum Actions {
		SINGLE_BUMP,
		SINGLE_SHAKE,
		SHAKING,
		ANYTHING_ELSE_I_DONT_NEED
	}
	public class AccelerationEvent {
		public float[] values = new float[3];
		public long time;
		AccelerationEvent(SensorEvent event) {
			values = event.values.clone();
			time = event.timestamp;
		}
	}
	Context mContext = null;
	private SensorManager mSensorManager = null;
	private Sensor mAccelerometer = null;
	private boolean mRunning = false;
	List<AccelerationEvent> mEventList = new ArrayList<AccelerationEvent>();
	
	public AccelerationProcessor(Context context) {
		mContext  = context;
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        Start();
	}
	
	public void Start() {
    	if (mAccelerometer!=null) mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    	mRunning = true;
	}

	public void Stop() {
		mSensorManager.unregisterListener(this);
		mRunning = false;
	}
	
	public boolean IsRunning() {
		return mRunning;
	}
	
	public void SaveToFile() {
		String prefsName = mContext.getString(R.string.DevPrefs);
		SharedPreferences devPrefs = mContext.getSharedPreferences(prefsName,Context.MODE_PRIVATE);
		if (devPrefs.getBoolean(mContext.getString(R.string.SaveTrackingSession), false)) {
			new AccelerationSerializer(this);
		}
	}
	
	public List<AccelerationEvent> GetEvents() {
		return mEventList;
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_LINEAR_ACCELERATION:
			mEventList.add(new AccelerationEvent(event));
			break;
		default:
			break;
		}
	}
}
