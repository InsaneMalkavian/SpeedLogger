package com.delin.speedlogger.Sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.ArrayList;
import java.util.List;

import com.delin.speedlogger.Serialization.AccelerationSerializer;

public class AccelerationProcessor implements SensorEventListener {
	public enum Actions {
		SINGLE_BUMP,
		SINGLE_SHAKE,
		SHAKING,
		ANYTHING_ELSE_I_DONT_NEED
	}
	class AccelerationEvent {
		float[] values = new float[3];
		long time;
		AccelerationEvent(SensorEvent event) {
			values = event.values.clone();
			time = event.timestamp;
		}
	}
	private SensorManager mSensorManager = null;
	private Sensor mAccelerometer = null;
	private boolean mRunning = false;
	List<AccelerationEvent> mEventList = new ArrayList<AccelerationEvent>();
	
	public AccelerationProcessor(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
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
		new AccelerationSerializer(this);
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
