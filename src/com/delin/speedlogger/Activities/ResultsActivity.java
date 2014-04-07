package com.delin.speedlogger.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.delin.speedlogger.R;
import com.delin.speedlogger.Results.MeasurementResult;
import com.delin.speedlogger.Results.ResultsManager;
import com.delin.speedlogger.Results.SessionResult;
import com.delin.speedlogger.Utils.Converter;

public class ResultsActivity extends Activity {
    class TableWrapper {
        TableRow row;
        TextView leftText;
        TextView rightText;
        TableWrapper (Context context, String leftText, String rightText) {
            row = new TableRow(context);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(new TableRow.LayoutParams(params));

            this.leftText = new TextView(context);
            this.rightText = new TextView(context);
            this.leftText.setText(leftText);
            this.rightText.setText(rightText);
            this.leftText.setGravity(Gravity.RIGHT);
            this.leftText.setTextAppearance(context, android.R.attr.textAppearanceSmall);
            this.leftText.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT, 0.5f));
            TableRow.LayoutParams param = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT, 0.5f);
            param.setMargins(getResources().getDimensionPixelSize(R.dimen.ResSpace), 0, 0, 0);
            this.rightText.setLayoutParams(param);
            row.addView(this.leftText);
            row.addView(this.rightText);
        }
    }
    private TextView mMaxSpeed;
    private TextView mDistance;
    private TextView mTime;
    private final static float SPEED_DIFF = 20.f;
    private final static float DISTANCES[] = {201.f, 402.f, 804.f, 1000.f, 1609.f};
    private final static String DISTANCES_STR[] = {"1/8 mile:", "1/4 mile:", "1/2 mile:", "1000m:", "1 mile:"};

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);
        Button btn = (Button)findViewById(R.id.buttonLocalTimes);
        btn.setOnClickListener(mOnClickListener);
        btn = (Button)findViewById(R.id.buttonTestAgain);
        btn.setOnClickListener(mOnClickListener);

        mMaxSpeed = (TextView)findViewById(R.id.textMaxSpeed);
        mDistance = (TextView)findViewById(R.id.textDistance);
        mTime = (TextView)findViewById(R.id.totalTimevalue);

        HandleResults();
    }

    public void HandleResults() {
        SessionResult result = new SessionResult(MeasurementResult.INSTANCE.getLocations());
        if (result.isValid() == false) {
            ShowZeroResults();
            return;
        }
        boolean isStraightLine = result.getStraightLine();
        boolean isDownhillOK = result.getDownhillOK();

        float maxSpeed = result.GetMaxSpeed();
        float totalRunDist = result.GetTotalDistance();
        mMaxSpeed.setText(String.format("%.3f", Converter.ms2kph(maxSpeed)) + " km/h");
        mDistance.setText(String.format("%.3f", totalRunDist) + " meters");
        mTime.setText(String.format("%.3f", (float)result.GetTotalTime()/1000) + " sec");
        ((CheckBox)findViewById(R.id.cbStraightLine)).setChecked(isStraightLine);
        ((CheckBox)findViewById(R.id.cbUphill)).setChecked(isDownhillOK);

        if (isStraightLine && isDownhillOK) { // Save result via ResultsManager
            ResultsManager.getInstance().addResult(result);
        }

        TableLayout table = (TableLayout)findViewById(R.id.TableLayout01);
        for (float speed = SPEED_DIFF; speed<=Converter.ms2kph(maxSpeed); speed+=SPEED_DIFF) {
            TableWrapper tableWrapperRow = new TableWrapper(this, "0 - " + Integer.toString((int)speed)+":",
                    String.format("%.3f", (float)result.GetTimeAtSpeed(Converter.kph2ms(speed))/1000) + " sec");
            table.addView(tableWrapperRow.row);
        }

        table = (TableLayout)findViewById(R.id.TableLayoutDistances);
        for (int i = 0; i<DISTANCES.length && DISTANCES[i]<=totalRunDist; i++) {
            TableWrapper tableWrapperRow = new TableWrapper(this, DISTANCES_STR[i],
                    String.format("%.3f", (float)result.GetTimeAtDistance(DISTANCES[i])/1000) + " sec");
            table.addView(tableWrapperRow.row);
        }
    }

    void ShowZeroResults() {
        // TODO: show "measurement failed" dialog instead
        mMaxSpeed.setText("0 kph");
        mDistance.setText("0 m");
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
            case R.id.buttonLocalTimes:
                intent = new Intent(v.getContext(), LocalTimesActivity.class);
                break;
            case R.id.buttonTestAgain:
                intent = new Intent(v.getContext(), SpeedLoggerActivity.class);
                break;
            }
            if (intent != null) startActivity(intent);
        }
    };
}
