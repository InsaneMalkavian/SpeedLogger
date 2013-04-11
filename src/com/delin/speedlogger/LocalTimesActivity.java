package com.delin.speedlogger;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class LocalTimesActivity extends Activity {
	TableLayout mHeaderTable;
	TableLayout mValuesTable;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_times);
        mHeaderTable = (TableLayout)findViewById(R.id.TableLayout1); // TODO: name me well
        mValuesTable = (TableLayout)findViewById(R.id.TableLayout2);
        
        ShowLocalResults();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_local_times, menu);
        return true;
    }
    
    void ShowLocalResults() {
    	List<SessionResult> results = ResultsManager.GetInstance().GetResults();
    	
    	TableRow row;
    	TextView text;
    	for(int i = 0; i<results.size(); ++i){
    		row = new TableRow(this);
    		text = new TextView(this);
            text.setText(Long.toString(results.get(i).mStartTime));
            row.addView(text);
            text = new TextView(this);
            text.setText(Float.toString(results.get(i).mDistance));
            row.addView(text);
            text = new TextView(this);
            text.setText(Float.toString(results.get(i).mMaxSpeed));
            row.addView(text);
            text = new TextView(this);
            text.setText(Long.toString(results.get(i).mDuration));
            row.addView(text);
            mValuesTable.addView(row);
    	}
    }
}
