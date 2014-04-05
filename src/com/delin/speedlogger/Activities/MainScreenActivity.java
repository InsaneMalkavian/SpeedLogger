package com.delin.speedlogger.Activities;

import com.delin.speedlogger.R;
import com.delin.speedlogger.Results.ResultsManager;
import com.delin.speedlogger.Utils.StorageProxy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainScreenActivity extends Activity {
	
	private ResultsManager mResultsManager;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);
        Button btn = (Button)findViewById(R.id.buttonStart);
        btn.setOnClickListener(mOnClickListener);
        btn = (Button)findViewById(R.id.buttonPreferences);
        btn.setOnClickListener(mOnClickListener);
        btn = (Button)findViewById(R.id.buttonDevPrefs);
        btn.setOnClickListener(mOnClickListener);
        btn = (Button)findViewById(R.id.buttonStatus);
        btn.setOnClickListener(mOnClickListener);
        btn = (Button)findViewById(R.id.buttonTest);
        btn.setOnClickListener(mOnClickListener);
        btn = (Button)findViewById(R.id.chartpage);
        btn.setOnClickListener(mOnClickListener);
        btn = (Button)findViewById(R.id.buttonLocalTimes);
        btn.setOnClickListener(mOnClickListener);
        
        StorageProxy.GetInstance(this);
        
        mResultsManager = ResultsManager.getInstance();
        // load data if it's the first time we came here
        if(savedInstanceState == null)
        mResultsManager.init(this);
    }

	private OnClickListener mOnClickListener = new OnClickListener() {
	    public void onClick(View v) {
	    	Intent intent = null;
	    	switch (v.getId()) {
	    	case R.id.buttonStart:
	    		intent = new Intent(v.getContext(), SpeedLoggerActivity.class);
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
	    	case R.id.buttonStatus: // just for test, should be removed
	    		intent = new Intent(v.getContext(), SensorStatusActivity.class);
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