package com.delin.speedlogger.Activities;

import java.util.List;

import com.delin.speedlogger.R;
import com.delin.speedlogger.Results.ResultsManager;
import com.delin.speedlogger.Results.SessionResult;
import com.delin.speedlogger.Results.StoredRecord;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class LocalTimesActivity extends Activity {
	TableLayout mHeaderTable;
	TableLayout mValuesTable;
	List<StoredRecord> mResults;
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
    
    void ShowLocalResults() {
    	mResults = ResultsManager.GetInstance().GetResults();
    	// clear widgets table (0st row used as header, don't touch it)
    	mValuesTable.removeViews(1, mValuesTable.getChildCount()-1);
    	// put new widgets
    	TableRow row;
    	TextView text;
    	for(int i = 0; i<mResults.size(); ++i){
    		Log.i("LocalResults", i + ")  " + mResults.get(i).toString());
    		row = new TableRow(this);
    		row.setId(i);
    		row.setOnClickListener(mOnClickListener);
    		text = new TextView(this);
            text.setText(Long.toString(mResults.get(i).GetStartTime()));
            row.addView(text);
            text = new TextView(this);
            text.setText(Float.toString(mResults.get(i).GetTotalDistance()));
            row.addView(text);
            text = new TextView(this);
            text.setText(Float.toString(mResults.get(i).GetMaxSpeed()));
            row.addView(text);
            text = new TextView(this);
            text.setText(Long.toString(mResults.get(i).GetTotalTime()));
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
        		StoredRecord result = mResults.get(v.getId());
        		ResultsManager.GetInstance().SetSelectedResult(result);
        		Intent intent = new Intent(v.getContext(), ResultDetailsActivity.class);
        		startActivity(intent);
        	}
        }
    };
}
