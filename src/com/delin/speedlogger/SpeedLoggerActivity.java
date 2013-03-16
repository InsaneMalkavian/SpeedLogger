package com.delin.speedlogger;

import java.util.List;

import com.delin.speedlogger.R;
import com.delin.speedlogger.TrackingSession.WarmupState;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SpeedLoggerActivity extends Activity implements TrackingSessionListener {
	
	Button mButton;
	TextView mTextView;
	TrackingSession mTrackingSession;
	MeasurementResult mMeasurement;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mButton = (Button)findViewById(R.id.button1);
        mTextView = (TextView)findViewById(R.id.textView1);
        mButton.setOnClickListener(mOnClickListener);

        mMeasurement = MeasurementResult.GetInstance();
        mTrackingSession = new TrackingSession(this);
        mTrackingSession.AddListener(this);
    }
    
    @Override
    public void onDestroy() {
    	mTrackingSession.RemoveListener(this);
    	mTrackingSession.StopService();
    	mTrackingSession = null;
    	super.onDestroy();
    }
    
    private OnClickListener mOnClickListener = new OnClickListener() {
        public void onClick(View v) {
        	Location location = mTrackingSession.GetLastLocation();
        	mTextView.setText(Logger.LocToStr(location));
        };
    };
        
    @Override
	public void onSessionWarmingUp(WarmupState mWarmupState) {
    	switch (mWarmupState)
    	{
    	case WAITING_FIX:
			mButton.setText("Warming up, please wait until proper fix will be received");	
			break;
    	case HIGH_SPEED:
    		mButton.setText("Stop moving before we can start.");
    	}
	}

	@Override
	public void onSessionReady() {
		mButton.setText("Ready to start, you can move now");		
	}

	@Override
	public void onSessionStart() {
		mButton.setText("Push, push, push");		
	}

	@Override
	public void onSessionFinished(List<Location> mLocList) {
		mMeasurement.SetLocations(mLocList);
		// TODO: maybe we can create it only once
		Intent intent = new Intent(this, ResultsActivity.class);
		startActivity(intent);	
	}

	@Override
	public void onSessionError() {
		mButton.setText("Error occured!");
	}

	@Override
	public void onSessionLocationUpdate(Location location) {
    	mTextView.setText(Logger.LocToStr(location));
	}

	@Override
	public void onSessionStopped() {
		// TODO Auto-generated method stub
		
	}
}