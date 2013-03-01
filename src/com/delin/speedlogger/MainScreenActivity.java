package com.delin.speedlogger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainScreenActivity extends Activity {
	Button mStartButton;
	Button mPreferencesButton;
	Button mLocationStatusButton;
	Button mTestButton;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);
        mStartButton = (Button)findViewById(R.id.buttonStart);
        mStartButton.setOnClickListener(mOnClickListener);
        mPreferencesButton = (Button)findViewById(R.id.buttonPreferences);
        mPreferencesButton.setOnClickListener(mOnClickListener);
        mTestButton = (Button)findViewById(R.id.buttonTest);
        mTestButton.setOnClickListener(mOnClickListener);
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
	    	case R.id.buttonTest: // just for test, should be removed
	    		intent = new Intent(v.getContext(), ResultsActivity.class);
	    		break;
	    	}
	    	if (intent != null) startActivity(intent);    		
	    }
	};
}