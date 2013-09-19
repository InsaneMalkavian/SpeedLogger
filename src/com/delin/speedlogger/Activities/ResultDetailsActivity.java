package com.delin.speedlogger.Activities;

import java.text.SimpleDateFormat;

import com.delin.speedlogger.R;
import com.delin.speedlogger.Results.ResultsManager;
import com.delin.speedlogger.Results.StoredRecord;
import com.delin.speedlogger.Utils.Converter;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ResultDetailsActivity extends Activity {
	private TextView mTotalDistance;
	private TextView mTotalTime;
	private TextView mMaxSpeed;
	private TextView mCarInfo;
	private Button mBackButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_details);
		mTotalDistance = (TextView)findViewById(R.id.textTotalDistance);
		mTotalTime = (TextView)findViewById(R.id.textTotalTime);
		mMaxSpeed = (TextView)findViewById(R.id.textMaxSpeed);
		mCarInfo = (TextView)findViewById(R.id.textCarInfo);
		mBackButton = (Button)findViewById(R.id.BackButton);
		mBackButton.setOnClickListener(mOnClickListener);
	    ShowDetails();
	}
	
	private OnClickListener mOnClickListener = new OnClickListener() {
	    public void onClick(View v) {
	    	switch (v.getId()) {
		    	case R.id.BackButton:
		    		ResultDetailsActivity.this.onBackPressed();
		    		break;
	    	}
	    }
	};
	
	void ShowDetails(){
		StoredRecord result = ResultsManager.GetInstance().GetSelectedResult();

    	mTotalDistance.setText(String.format("%.3f", result.GetTotalDistance()) + " meters");
    	mTotalTime.setText(String.format("%.3f", (float)result.GetTotalTime()/1000) + " sec");
    	mMaxSpeed.setText(String.format("%.3f", Converter.ms2kph(result.GetMaxSpeed())) + " km/h");
    	mCarInfo.setText(result.GetCar().GetBrand() + " " +
    					 result.GetCar().GetModel() + " " +
    					 result.GetCar().GetModelIndex() + ", " +
    					 Long.toString(result.GetCar().GetHorsePower()) + " HP");
	}

}
