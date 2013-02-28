package com.delin.speedlogger;

import java.util.List;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ResultsActivity extends Activity {
	TextView maxSpeedText;
	TextView distanceText;
	Button mButton;
	String testline = "origin";
	MeasurementResult mMeasurement;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);
		mButton = (Button)findViewById(R.id.button1);
		mButton.setOnClickListener(mOnClickListener);
		maxSpeedText = (TextView)findViewById(R.id.textDistance);
		distanceText = (TextView)findViewById(R.id.textMaxSpeed);
		maxSpeedText.setText(testline);
		
		mMeasurement = MeasurementResult.GetInstance();
		mMeasurement.SaveToGPX("/mnt/sdcard/textlog.gpx");
	}
	 
	public double Distance(Location loc1, Location loc2) {
		double x1 = loc1.getLatitude(); double y1 = loc1.getLongitude();
		double x2 = loc2.getLatitude(); double y2 = loc2.getLongitude();
		return Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
	}
	
	public void onSessionFinished(List<Location> mLocList){
		// printing max speed
		// TODO: we can get maxSpeed from TrackingSession
		float maxSpeed = mLocList.get(0).getSpeed();
		for (int i=1; i<mLocList.size(); i++){
			if (maxSpeed < mLocList.get(i).getSpeed()){
				maxSpeed = mLocList.get(i).getSpeed();
			}
		}
		maxSpeedText.setText(Float.toString(maxSpeed));
		// interpolate here
		
		// printing distance
		Location origin = mLocList.get(0);
		Location dest = mLocList.get(mLocList.size());
		double distance = Distance(origin, dest);
		distanceText.setText(Double.toString(distance));
	}
	
	private void TestFunc(String input) {
		input = "hardcode";
	}
	
    private OnClickListener mOnClickListener = new OnClickListener() {
        public void onClick(View v) {
        	TestFunc(testline);
        	maxSpeedText.setText(testline);
        }
        };

}
