package com.delin.speedlogger.Activities;

import com.delin.speedlogger.R;
import com.delin.speedlogger.Results.ResultsManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainScreenActivity extends Activity {
	Button mStartButton;
	Button mPreferencesButton;
	Button mDevPrefsButton;
	Button mLocationStatusButton;
	Button mTestButton;
	Button mChartPage;
	Button mLocalTimesButton;
	
	ResultsManager mResultsManager;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);
        mStartButton = (Button)findViewById(R.id.buttonStart);
        mStartButton.setOnClickListener(mOnClickListener);
        mPreferencesButton = (Button)findViewById(R.id.buttonPreferences);
        mPreferencesButton.setOnClickListener(mOnClickListener);
        mDevPrefsButton = (Button)findViewById(R.id.buttonDevPrefs);
        mDevPrefsButton.setOnClickListener(mOnClickListener);
        mTestButton = (Button)findViewById(R.id.buttonTest);
        mTestButton.setOnClickListener(mOnClickListener);
        mChartPage = (Button)findViewById(R.id.chartpage);
        mChartPage.setOnClickListener(mOnClickListener);
        mLocalTimesButton = (Button)findViewById(R.id.buttonLocalTimes);
        mLocalTimesButton.setOnClickListener(mOnClickListener);
        
        mResultsManager = ResultsManager.GetInstance();
        // load data if it's the first time we came here
        if(savedInstanceState == null)
        mResultsManager.Init(this);
    }

	private OnClickListener mOnClickListener = new OnClickListener() {
	    public void onClick(View v) {
	    	Intent intent = null;
	    	switch (v.getId()) {
	    	case R.id.buttonStart:
	    		intent = new Intent(v.getContext(), SpeedLoggerActivity.class);
	    		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); // don't keep on stack
	    		break;
	    	case R.id.buttonPreferences:
	    		intent = new Intent(v.getContext(), PreferencesActivity.class);
	    		break;
	    	case R.id.buttonDevPrefs:
	    		intent = new Intent(v.getContext(), DevPrefsActivity.class);
	    		break;
	    	case R.id.buttonTest: // just for test, should be removed
	    		intent = new Intent(v.getContext(), ResultsActivity.class);
	    		break;
	    	case R.id.chartpage: // just for test, should be removed
	    		intent = new Intent(v.getContext(), XYChartBuilder.class);
	    		break;
	    	case R.id.buttonLocalTimes:
	    		intent = new Intent(v.getContext(), LocalTimesActivity.class);
	    		break;
	    	}
	    	if (intent != null) startActivity(intent);    		
	    }
	};
}