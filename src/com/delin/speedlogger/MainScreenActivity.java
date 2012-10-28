package com.delin.speedlogger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainScreenActivity extends Activity {
	Button mStartButton;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);
        mStartButton = (Button)findViewById(R.id.buttonStart);
        mStartButton.setOnClickListener(mOnClickListener);
    }

	private OnClickListener mOnClickListener = new OnClickListener() {
	    public void onClick(View v) {
	        Intent intent = new Intent(v.getContext(), SpeedLoggerActivity.class);
	        startActivity(intent);	    	
	    }
	};
}