package com.delin.speedlogger.Activities;

import java.util.List;

import com.delin.speedlogger.R;
import com.delin.speedlogger.Results.ResultsManager;
import com.delin.speedlogger.Results.SessionResult;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class LocalTimesActivity extends Activity {
	TableLayout mHeaderTable;
	TableLayout mValuesTable;
	List<SessionResult> mResults;
	Button mClearButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_times);
        mHeaderTable = (TableLayout)findViewById(R.id.TableLayout1); // TODO: name me well
        mValuesTable = (TableLayout)findViewById(R.id.TableLayout2);
        mClearButton = (Button)findViewById(R.id.buttonClearDatabase);
        mClearButton.setOnClickListener(mOnClickListener);
        ShowLocalResults();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_local_times, menu);
        return true;
    }
    
    void ShowLocalResults() {
    	mResults = ResultsManager.GetInstance().GetResults();
    	// clear widgets table (0st row used as header, don't touch it)
    	mValuesTable.removeViews(1, mValuesTable.getChildCount()-1);
    	// put new widgets
    	TableRow row;
    	TextView text;
    	for(int i = 0; i<mResults.size(); ++i){
    		row = new TableRow(this);
    		row.setId(i);
    		row.setOnClickListener(mOnClickListener);
    		text = new TextView(this);
            text.setText(Long.toString(mResults.get(i).mStartTime));
            row.addView(text);
            text = new TextView(this);
            text.setText(Float.toString(mResults.get(i).mDistance));
            row.addView(text);
            text = new TextView(this);
            text.setText(Float.toString(mResults.get(i).mMaxSpeed));
            row.addView(text);
            text = new TextView(this);
            text.setText(Long.toString(mResults.get(i).mDuration));
            row.addView(text);
            mValuesTable.addView(row);
    	}
    }
    
    private OnClickListener mOnClickListener = new OnClickListener() {
        public void onClick(View v) {
        	if(v.getId() == R.id.buttonClearDatabase){
        		ResultsManager.GetInstance().ClearLocalResults();
        		ShowLocalResults();
        	}
        	else{
        		SessionResult result = mResults.get(v.getId());
        		Log.i("LocalTimesActivity", result.GetLocations().toString());
        		// TODO: here we go to the result details page (chart + stuff)
        	}
        }
    };
}
