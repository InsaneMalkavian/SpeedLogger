package com.delin.speedlogger;

import java.util.List;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.widget.CheckBox;
import android.util.Log;
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
		mMaxSpeed.setText(testline);
		mDistance = (TextView)findViewById(R.id.textDistance);
        mStraightLine = (CheckBox)findViewById(R.id.cbStraightLine);
		
		mMeasurement = MeasurementResult.GetInstance();
		mMeasurement.SaveToGPX("/mnt/sdcard/textlog.gpx");
		
		ShowResults();
	}
	
	public void ShowResults(){
		List<Location> locList = mMeasurement.GetLocations();
		SessionResult result = new SessionResult(locList);
		boolean isStraightLine = Geometry.StraightLine(locList,false);
		
		mMaxSpeed.setText(Double.toString(result.mMaxSpeed*3.6) + " kph"); // TODO: 3.6 must be a constant
		mDistance.setText(Double.toString(result.mDistance));
		mStraightLine.setChecked(isStraightLine);
		
		if(isStraightLine){
			// Save result via ResultsManager
		}
		
		Log.i("Results Activity","Locs in path: " + Integer.toString(locList.size()));
		for(int i=0; i<locList.size(); ++i){
			Log.i("Results Activity", "-----\n" + "#" + Integer.toString(i) + 
				  "\n" + Logger.LocToStr(locList.get(i)));
		}
		// TODO: make use of interpolation; now it only shows values
		Interpolator interp = new Interpolator(locList);
		Location loc;
		for(long i = locList.get(0).getTime(); i <= locList.get(locList.size()-1).getTime(); i += 250){
			loc = interp.SpeedByTime(i);
			Log.i("Results Activity", "-----\n" + "time: " + Long.toString(i) + 
					  "  speed: " + Double.toString(loc.getSpeed()*3.6));
		}
		for(float i = locList.get(0).getSpeed(); i <= locList.get(locList.size()-1).getSpeed(); i += 0.5){
			loc = interp.TimeBySpeed(i);
			Log.i("Results Activity", "-----\n" + "speed: " + Double.toString(i*3.6) + 
					  "  time: " + Long.toString(loc.getTime()));
		}
	}
	
	private void TestFunc(String input) {
		input = "hardcode";
	}
	
    private OnClickListener mOnClickListener = new OnClickListener() {
        public void onClick(View v) {
        	TestFunc(testline);
        	mMaxSpeed.setText(testline);
        	mStraightLine.setChecked(!mStraightLine.isChecked());
        	mStraightLine.setText("hello kitty");
        	
        }
    };
}
