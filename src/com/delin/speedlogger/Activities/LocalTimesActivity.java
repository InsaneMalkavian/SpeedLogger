package com.delin.speedlogger.Activities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.delin.speedlogger.R;
import com.delin.speedlogger.Results.ResultsManager;
import com.delin.speedlogger.Results.SessionResult;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class LocalTimesActivity extends Activity {
    private TableLayout mValuesTable;
    private List<SessionResult> mResults;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_times);
        mValuesTable = (TableLayout)findViewById(R.id.TableLayout2);
        Button btn = (Button)findViewById(R.id.buttonClearDatabase);
        btn.setOnClickListener(mOnClickListener);
        ShowLocalResults();
    }

    void ShowLocalResults() {
        mResults = ResultsManager.getInstance().getResults();
        // clear widgets table (0st row used as header, don't touch it)
        mValuesTable.removeViews(1, mValuesTable.getChildCount()-1);
        // put new widgets
        TableRow row;
        TextView text;
        for(int i = 0; i<mResults.size(); ++i) {
            row = new TableRow(this);
            row.setId(i);
            row.setOnClickListener(mOnClickListener);
            text = new TextView(this);
            Date someDate = new Date(mResults.get(i).GetStartTime());
            SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            text.setText(df.format(someDate));
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
            if(v.getId() == R.id.buttonClearDatabase) {
                ResultsManager.getInstance().clearLocalResults();
                ShowLocalResults();
            }
            else {
                SessionResult result = mResults.get(v.getId());
                Log.i("LocalTimesActivity", result.getLocations().toString());
                // TODO: here we go to the result details page (chart + stuff)
            }
        }
    };
}
