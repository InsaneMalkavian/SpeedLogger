package com.delin.speedlogger.Activities;

import com.delin.speedlogger.R;
import com.delin.speedlogger.Results.ResultsManager;
import com.delin.speedlogger.Results.SessionResult;
import com.delin.speedlogger.TrackingSession.MeasurementResult;
import com.delin.speedlogger.Utils.Converter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ResultsActivity extends Activity {
    class TableWrapper {
        TableRow row;
        TextView mLeftText;
        TextView mRightText;
        TableWrapper (LinearLayout parent, Context context, String leftText, String rightText) {
            row = new TableRow(context);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(new TableRow.LayoutParams(params));
            parent.addView(row);
            
            mLeftText = new TextView(context);
            mRightText = new TextView(context);
            mLeftText.setText(leftText);
            mRightText.setText(rightText);
            mLeftText.setGravity(Gravity.RIGHT);
            mLeftText.setTextAppearance(context, android.R.attr.textAppearanceSmall);
            mLeftText.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT, 0.5f));
            TableRow.LayoutParams param = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT, 0.5f);
            
            param.setMargins(getResources().getDimensionPixelSize(R.dimen.ResSpace), 0, 0, 0);
            mRightText.setLayoutParams(param);
            row.addView(mLeftText);
            row.addView(mRightText);
        }
    }
    TextView mMaxSpeed;
    TextView mDistance;
    TextView mTime;
    TextView mZero60;
    TextView mZero80;
    TextView mZero100;
    TextView[] mSpeedsViews = null;
    TableWrapper mTableWrapperRow;
    float[] mSpeeds = {60.f, 80.f, 100.f, 120.f, 150.f, 180.f, 200.f, 250.f, 300.f, 400.f}; // kmph
    float[] mDistances = {100.f, 200.f, 400.f, 800.f, 1000.f}; // meters
    
    
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
        mZero60 = (TextView)findViewById(R.id.zero60value);
        mZero80 = (TextView)findViewById(R.id.zero80value);
        mZero100 = (TextView)findViewById(R.id.zero100value);
        
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
        float speeds = Converter.kph2ms(60);
        if (speeds<maxSpeed) {
            float time = (float)result.GetTimeAtSpeed(speeds)/1000;
            mZero60.setText(String.format("%.3f", time)+" sec");
        }
        speeds = Converter.kph2ms(80);
        if (speeds<maxSpeed) {
            float time = (float)result.GetTimeAtSpeed(speeds)/1000;
            mZero80.setText(String.format("%.3f", time)+" sec");
        }
        speeds = Converter.kph2ms(100);
        if (speeds<maxSpeed) {
            float time = (float)result.GetTimeAtSpeed(speeds)/1000;
            mZero100.setText(String.format("%.3f", time)+" sec");
        }
        
        TableLayout table = (TableLayout)findViewById(R.id.TableLayout01);
        mTableWrapperRow = new TableWrapper(table, this, "Left", "Right");
        
        Button button = new Button(this);
        button.setText("One more Button");
        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        LinearLayout buttons = (LinearLayout)findViewById(R.id.LinearLayout01);
        buttons.addView(button);
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
        mZero60.setText("N/A");
        mZero100.setText("N/A");
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
