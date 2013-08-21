package com.delin.speedlogger.Activities;

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
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delin.speedlogger.R;
import com.delin.speedlogger.Sensors.AccelerationProcessor;
import com.delin.speedlogger.TrackingSession.MeasurementResult;
import com.delin.speedlogger.TrackingSession.TrackingSession;
import com.delin.speedlogger.TrackingSession.TrackingSession.WarmupState;
import com.delin.speedlogger.TrackingSession.TrackingSessionListener;
import com.delin.speedlogger.Utils.Converter;
import com.delin.speedlogger.Utils.Logger;

public class SpeedLoggerActivity extends Activity implements TrackingSessionListener {
	
	TextView mTVHeader;
	TextView mTVUNderDash;
	TrackingSession mTrackingSession;
	MeasurementResult mMeasurement;
	AccelerationProcessor mAccel;
	boolean mTracking = false; // indicates user in tracking mode. We only set it to true and don't need to set false anymore

	XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	XYSeries mCurrentSeries = null;
	GraphicalView mChartView = null;	
	
	long mStartTime=0;
	
	private int mChronoInterval = 15; // 15 milliseconds by default
	private Handler mChronoHandler = new Handler(); // timer is used to draw time since start
	

	float mPreviousAngle = -90.0f;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
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
        
        
        SharedPreferences prefs = getSharedPreferences(getString(R.string.Prefs),Context.MODE_PRIVATE);
		if (prefs.getBoolean(getString(R.string.ScreenAwake), false))
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
        mTVHeader = (TextView)findViewById(R.id.statusHeader);
        mTVUNderDash = (TextView)findViewById(R.id.tvUnderDash);

        mMeasurement = MeasurementResult.GetInstance();
        mTrackingSession = new TrackingSession(this);
        mTrackingSession.AddListener(this);
        mAccel = new AccelerationProcessor(this);
        
        RotateAnimation rotateAnimation1 = new RotateAnimation(mPreviousAngle, -138.f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation1.setInterpolator(new LinearInterpolator());
        long duration = 750;
		rotateAnimation1.setDuration(duration);
		rotateAnimation1.setFillAfter(true);
        View img = (ImageView)findViewById(R.id.imageView2);
		img .startAnimation(rotateAnimation1);
		mPreviousAngle=-138.f+0*2;
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	mTrackingSession.StopService();
    	mAccel.Stop();
    }
    

    @Override
    protected void onResume() {
    	super.onResume();
    	mCurrentSeries.clear();		
		if (mChartView == null) {
    		LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
    		mChartView = ChartFactory.getLineChartView(this, mDataset, mRenderer);
    		mRenderer.setClickEnabled(true);
    		mRenderer.setSelectableBuffer(100);
    		layout.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    	} else {
    		mChartView.repaint();
    	}
    	mCurrentSeries.clear();
    	mTrackingSession.StartService();
    	mAccel.Start();
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	mTrackingSession.RemoveListener(this);
    	mTrackingSession = null;
    }
    
    private OnClickListener mOnClickListener = new OnClickListener() {
        public void onClick(View v) {
        	Location location = mTrackingSession.GetLastLocation();
        	mTVHeader.setText(Logger.LocToStr(location));
        };
    };
        
    @Override
	public void onSessionWarmingUp(WarmupState mWarmupState) {
    	switch (mWarmupState)
    	{
    	case NO_GPS:
    		mTVHeader.setText("GPS Service disabled, please enable GPS");
    		break;
    	case WAITING_FIX:
    		mTVHeader.setText("Warming up, please wait until fix will be received");
			break;
    	case BAD_ACCURACY:
    		mTVHeader.setText("Inaccurate fix, please wait for stable fix");
			break;
    	case HIGH_SPEED:
    		mTVHeader.setText("Stop moving before we can start.");
    		break;
    	}
	}

	@Override
	public void onSessionReady() {
		mTVHeader.setText("Ready to start, you can move now");		
	}

	@Override
	public void onSessionStart(long startTime) {
		mTracking = true;
		mTVHeader.setText("Push, push, push");
		mStartTime = startTime;
		mChronoChecker.run(); 
		if (mCurrentSeries!=null && mChartView!=null && mTracking) {
			mCurrentSeries.add(0, Converter.ms2kph(0));
			mCurrentSeries.add((mTrackingSession.GetLastLocation().getTime()-mStartTime)/1000, Converter.ms2kph(mTrackingSession.GetLastLocation().getSpeed()));
        	mChartView.repaint();
        }
	}

	@Override
	public void onSessionFinished(List<Location> locList) {
		mTracking = false;
		//mChronoHandler.removeCallbacks(mChronoChecker);
		mAccel.Stop();
		mAccel.SaveToFile();
		mMeasurement.SetLocations(locList);
		Intent intent = new Intent(this, ResultsActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onSessionError() {
		mTracking = false;
		mTVHeader.setText("Error occured!");
	}

	@Override
	public void onSessionStopped() {
		mTracking = false;
	}
	
	@Override
	public void onSessionLocationUpdate(Location location) {
		final float mult = 3.6f*90/77;
    	//mTextView.setText(Logger.LocToStr(location));
		if (mCurrentSeries!=null && mChartView!=null && mTracking) {
			mCurrentSeries.add((location.getTime()-mStartTime)/1000, Converter.ms2kph(location.getSpeed()));        
        	mChartView.repaint();
        }
        
        RotateAnimation rotateAnimation1 = new RotateAnimation(mPreviousAngle, -138.f+location.getSpeed()*mult,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation1.setInterpolator(new LinearInterpolator());
        long duration = 975;
		rotateAnimation1.setDuration(duration);
		rotateAnimation1.setFillAfter(true);
        View img = (ImageView)findViewById(R.id.imageView2);
		img .startAnimation(rotateAnimation1);
		mPreviousAngle=-138.f+location.getSpeed()*mult;
	}

	Runnable mChronoChecker = new Runnable() {
	     @Override 
	     public void run() {
	    	 mTVUNderDash.setText(Long.toString(System.currentTimeMillis() - mStartTime));
	    	 mChronoHandler.postDelayed(mChronoChecker, mChronoInterval);
	     }
	};
}