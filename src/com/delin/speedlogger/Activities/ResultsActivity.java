package com.delin.speedlogger.Activities;

import com.delin.speedlogger.R;
import com.delin.speedlogger.Math.Geometry;
import com.delin.speedlogger.Results.ResultsManager;
import com.delin.speedlogger.Results.SessionResult;
import com.delin.speedlogger.TrackingSession.MeasurementResult;
import com.delin.speedlogger.Utils.Converter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ResultsActivity extends Activity {
	TextView mMaxSpeed;
	TextView mDistance;
	TextView mTime;
	TextView mZero60;
	TextView mZero100;
	CheckBox mStraightLine;
	Button mLocalTimesButton;
	String testline = "origin";
	MeasurementResult mMeasurement;
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);
		mLocalTimesButton = (Button)findViewById(R.id.buttonLocalTimes);
		mLocalTimesButton.setOnClickListener(mOnClickListener);
		mMaxSpeed = (TextView)findViewById(R.id.textMaxSpeed);
		mMaxSpeed.setText(testline);
		mDistance = (TextView)findViewById(R.id.textDistance);
		mTime = (TextView)findViewById(R.id.totalTimevalue);
		mZero60 = (TextView)findViewById(R.id.zero60value);
		mZero100 = (TextView)findViewById(R.id.zero100value);
        mStraightLine = (CheckBox)findViewById(R.id.cbStraightLine);
		
		mMeasurement = MeasurementResult.GetInstance();
		
		// if activity just created, not restored by system
		if(savedInstanceState == null) HandleResults();
	}
	
	public void HandleResults() {
		SessionResult result = new SessionResult(mMeasurement.GetLocations());
		if (result.IsValid() == false) {
			ShowZeroResults();
			return;
		}
		boolean isStraightLine = Geometry.StraightLine(result.GetLocations(),false);
		
		float maxSpeed = result.GetMaxSpeed();
		mMaxSpeed.setText(Double.toString(Converter.ms2kph(maxSpeed)) + " kph");
		mDistance.setText(Double.toString(result.GetTotalDistance()) + " m");
		mTime.setText(Float.toString((float)result.GetTotalTime()/1000) + " sec");
		mStraightLine.setChecked(isStraightLine);
		
		if(isStraightLine){ // Save result via ResultsManager
			ResultsManager.GetInstance().AddResult(result);
		}
		
		/*Log.i("Results Activity","Locs in path: " + Integer.toString(locList.size()));
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
					  "  speed: " + Double.toString(Converter.ms2kph(loc.getSpeed())));
		}
		for(float i = locList.get(0).getSpeed(); i <= locList.get(locList.size()-1).getSpeed(); i += 0.5){
			loc = interp.TimeBySpeed(i);
			Log.i("Results Activity", "-----\n" + "speed: " + Double.toString(Converter.ms2kph(i)) + 
					  "  time: " + Long.toString(loc.getTime()));
		}

		Location atSpeed = interp.TimeBySpeed(Converter.kph2ms(maxSpeed));
		mTime.setText(Float.toString((float)(atSpeed.getTime()-locList.get(0).getTime())/1000)+" sec");
		*/
		float speeds = Converter.kph2ms(60);
		if (speeds<maxSpeed) {
			float time = Converter.ms2kph(result.GetTimeAtSpeed(speeds));
			mZero60.setText(Float.toString(time)+" sec");
		}
		speeds = Converter.kph2ms(100);
		if (speeds<maxSpeed) {
			float time = Converter.ms2kph(result.GetTimeAtSpeed(speeds));
			mZero100.setText(Float.toString(time)+" sec");
		}
	}
	
	void ShowZeroResults() {
		// TODO: show "measurement failed" dialog instead
		mMaxSpeed.setText("0 kph");
		mDistance.setText("0 m");
		mZero60.setText("N/A");
		mZero100.setText("N/A");
		mStraightLine.setChecked(false);
	}
	
    private OnClickListener mOnClickListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent intent = null;
	    	switch (v.getId()) {
	    	case R.id.buttonLocalTimes:
	    		intent = new Intent(v.getContext(), LocalTimesActivity.class);
	    		break;
	    	}
	    	if (intent != null) startActivity(intent);   
        }
    };
}
