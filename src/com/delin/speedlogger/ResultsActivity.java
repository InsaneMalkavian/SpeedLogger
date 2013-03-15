package com.delin.speedlogger;

import java.util.List;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.widget.CheckBox;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ResultsActivity extends Activity {
	TextView mMaxSpeed;
	TextView mDistance;
	CheckBox mStraightLine;
	Button mButton;
	String testline = "origin";
	MeasurementResult mMeasurement;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);
		mButton = (Button)findViewById(R.id.button1);
		mButton.setOnClickListener(mOnClickListener);
		mMaxSpeed = (TextView)findViewById(R.id.textMaxSpeed);
		mDistance = (TextView)findViewById(R.id.textDistance);
        mMaxSpeed.setText(testline);
		
		mMeasurement = MeasurementResult.GetInstance();
		mMeasurement.SaveToGPX("/mnt/sdcard/textlog.gpx");
	}
	
	// TODO: this function will never be triggered
	// because ResultsActivity created _after_ onSessionFinishedCall.
	// call this function from OnCreate
	public void onSessionFinished(List<Location> mLocList){
		// printing max speed
		// TODO: we can get maxSpeed from TrackingSession
		float maxSpeed = mLocList.get(0).getSpeed();
		for (int i=1; i<mLocList.size(); i++){
			if (maxSpeed < mLocList.get(i).getSpeed()){
				maxSpeed = mLocList.get(i).getSpeed();
			}
		}
		mMaxSpeed.setText(Float.toString(maxSpeed));
		// interpolate here
		
		// printing distance
		Location origin = mLocList.get(0);
		Location dest = mLocList.get(mLocList.size()-1);
		double distance = origin.distanceTo(dest);
		mDistance.setText(Double.toString(distance));
		
		mStraightLine.setChecked(Geometry.StraightLine(mLocList,true));
	}
	
	private void TestFunc(String input) {
		input = "hardcode";
	}
	
    private OnClickListener mOnClickListener = new OnClickListener() {
        public void onClick(View v) {
        	TestFunc(testline);
        	mMaxSpeed.setText(testline);
        }
    };
}
