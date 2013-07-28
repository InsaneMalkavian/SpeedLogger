package com.delin.speedlogger.Activities;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.delin.speedlogger.R;

public class SensorStatusActivity extends Activity implements SensorEventListener {
	
	XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	XYSeries mCurrentSeries = null;
	GraphicalView mChartView = null;

	private SensorManager mSensorManager = null;
	private Sensor mAccelerometer = null;
	private long mStarttime = 0;
	private int mCurrentAxis = 0;
	private long mCount = 0;
	private double mSum = 0;
	private double mMX = 0;
	private double mDX = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensorstatus);
        
        Button clearButton = (Button)findViewById(R.id.buttonClear);
        clearButton.setOnClickListener(mOnClickListener);
        
    	Spinner spinner = (Spinner) findViewById(R.id.spinner1);
    	List<String> list = new ArrayList<String>();
    	list.add("X 1");
    	list.add("Y 2");
    	list.add("Z 3");
    	list.add("All");
    	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
    	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spinner.setAdapter(dataAdapter);
    	spinner.setOnItemSelectedListener(mOnItemSelectedListener);
    	
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
        mRenderer.setAxisTitleTextSize(16);
        mRenderer.setChartTitleTextSize(20);
        mRenderer.setLabelsTextSize(15);
        mRenderer.setLegendTextSize(15);
        mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
        //mRenderer.setZoomButtonsVisible(true);
        mRenderer.setPointSize(5);
        
        // add series
        String seriesTitle = "Series " + (mDataset.getSeriesCount() + 1);
        XYSeries series = new XYSeries(seriesTitle);
        mDataset.addSeries(series);
        mCurrentSeries = series;
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setFillPoints(true);
        
        
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }
    

    @Override
    protected void onResume() {
    	super.onResume();
    	if (mChartView == null) {
    		LinearLayout layout = (LinearLayout) findViewById(R.id.accelchart);
    		mChartView = ChartFactory.getLineChartView(this, mDataset, mRenderer);
    		mRenderer.setClickEnabled(true);
    		mRenderer.setSelectableBuffer(100);
    		layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    	} else {
    		mChartView.repaint();
    	}
    	RestartSession();
    	if (mAccelerometer!=null) mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
    	super.onPause();
    	mSensorManager.unregisterListener(this);
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// check for accelerometer
		float value = mCurrentAxis == 3 ? event.values[0]+event.values[1]+event.values[2] : event.values[mCurrentAxis];
		//mCurrentSeries.add((event.timestamp-mStarttime)/1000000L, value);
		mCurrentSeries.add(mCount, value);
    	mChartView.repaint();
    	mCount++;
    	mSum+=value;
    	mMX = mSum/mCount;
    	if (mCount%20==0) { // update values on display
    		
    	}
	}
	
	private void RestartSession() {
		mCurrentSeries.clear();
		mChartView.repaint();
    	mStarttime = System.nanoTime();
    	
    	mCount = 0;
    	mSum = 0;
    	mMX = 0;
    	mDX = 0;

	}
	
	private OnClickListener mOnClickListener = new OnClickListener() {
	    public void onClick(View v) {
	    	switch (v.getId()) {
	    	case R.id.buttonClear:
	    		RestartSession();
	    		break;
	    	}
	    }
	};
	
	private OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
			Toast.makeText(parent.getContext(), 
					"OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
					Toast.LENGTH_SHORT).show();
			if (parent.getItemAtPosition(pos).toString()=="X 1") mCurrentAxis = 0;
			if (parent.getItemAtPosition(pos).toString()=="Y 2") mCurrentAxis = 1;
			if (parent.getItemAtPosition(pos).toString()=="Z 3") mCurrentAxis = 2;
			if (parent.getItemAtPosition(pos).toString()=="All") mCurrentAxis = 3;
			RestartSession();
		}
		
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {			
		}
	};
}
