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
    private final static float speedDiff = 20.f;

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
        mMaxSpeed.setText(String.format("%.3f", Converter.ms2kph(maxSpeed)) + " km/h");
        mDistance.setText(String.format("%.3f", result.GetTotalDistance()) + " meters");
        mTime.setText(String.format("%.3f", (float)result.GetTotalTime()/1000) + " sec");
        ((CheckBox)findViewById(R.id.cbStraightLine)).setChecked(isStraightLine);
        ((CheckBox)findViewById(R.id.cbUphill)).setChecked(isDownhillOK);

        if (isStraightLine && isDownhillOK) { // Save result via ResultsManager
            ResultsManager.getInstance().addResult(result);
        }

        /*Log.i("Results Activity","Locs in path: " + Integer.toString(locList.size()));
        for(int i=0; i<locList.size(); ++i){
            Log.i("Results Activity", "-----\n" + "#" + Integer.toString(i) +
                  "\n" + Logger.LocToStr(locList.get(i)));
        }
        // TODO: make use of interpolation; now it only shows values
        Interpolator interp = new Interpolator(locList);
        Location loc;
        for(long i = locList.get(0).getTime(); i <= locList.get(locList.size()-1).getTime(); i += 250){
            loc = interp.SpeedByTime(i);
            Log.i("Results Activity", "-----\n" + "time: " + Long.toString(i) +
                      "  speed: " + Double.toString(Converter.ms2kph(loc.getSpeed())));
        }
        for(float i = locList.get(0).getSpeed(); i <= locList.get(locList.size()-1).getSpeed(); i += 0.5){
            loc = interp.TimeBySpeed(i);
            Log.i("Results Activity", "-----\n" + "speed: " + Double.toString(Converter.ms2kph(i)) +
                      "  time: " + Long.toString(loc.getTime()));
        }

        Location atSpeed = interp.TimeBySpeed(Converter.kph2ms(maxSpeed));
        mTime.setText(Float.toString((float)(atSpeed.getTime()-locList.get(0).getTime())/1000)+" sec");
        */

        TableLayout table = (TableLayout)findViewById(R.id.TableLayout01);

        for (float speed = speedDiff; speed<=Converter.ms2kph(maxSpeed); speed+=speedDiff) {
            TableWrapper tableWrapperRow = new TableWrapper(this, "0 - " + Integer.toString((int)speed)+":",
                    String.format("%.3f", (float)result.GetTimeAtSpeed(Converter.kph2ms(speed))/1000) + " sec");
            table.addView(tableWrapperRow.row);
        }

        /*Button button = new Button(this);
        button.setText("One more Button");
        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        LinearLayout buttons = (LinearLayout)findViewById(R.id.LinearLayout01);
        buttons.addView(button);*/
/*        TextView mLeftText = new TextView(this);
        mLeftText.setText("Left");
        mLeftText.setGravity(Gravity.RIGHT);
        mLeftText.setTextAppearance(this, android.R.attr.textAppearanceSmall);
        mLeftText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));

        buttons.addView(mLeftText);
*/
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
