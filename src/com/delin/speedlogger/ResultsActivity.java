package com.delin.speedlogger;

import java.util.List;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

public class ResultsActivity extends Activity {
	TextView maxSpeedText;
	TextView distanceText;
	CheckBox cbStraightLine;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainscreen);
		maxSpeedText = (TextView)findViewById(R.id.textDistance);
		distanceText = (TextView)findViewById(R.id.textMaxSpeed);
	}
	 
	public double Distance(Location loc1, Location loc2) {
		double x1 = loc1.getLatitude(); double y1 = loc1.getLongitude();
		double x2 = loc2.getLatitude(); double y2 = loc2.getLongitude();
		return Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
	}
	
	public boolean StraightLine3D(List<Location> locList) {
		if (locList.size() <= 2)
			return true;
		else {
			double x1 = locList.get(0).getLatitude();
			double y1 = locList.get(0).getLongitude();
			double z1 = locList.get(0).getAltitude();
			double x2 = locList.get(1).getLatitude();
			double y2 = locList.get(1).getLongitude();
			double z2 = locList.get(1).getAltitude();
			
			double dx = x2 - x1;
			double dy = y2 - y1;
			double dz = z2 - z1;
			
			// TODO: I have no idea what eps should be
			double eps = 0.001;
			for (int i=2; i<locList.size(); i++){
				double x = locList.get(i).getLatitude();
				double y = locList.get(i).getLongitude();
				double z = locList.get(i).getAltitude();
				
				if  ((x1-x)/dx - (y1-y)/dy >= eps 
					|| (x1-x)/dx - (z1-z)/dz >= eps)
				{
					return false;
				}
			}
			return true;
		}
	}
	
	public void onSessionFinished(List<Location> mLocList){
		// printing max speed
		// TODO: we can get maxSpeed from TrackingSession
		float maxSpeed = mLocList.get(0).getSpeed();
		for (int i=1; i<mLocList.size(); i++){
			if (maxSpeed < mLocList.get(i).getSpeed()){
				maxSpeed = mLocList.get(i).getSpeed();
			}
		}
		maxSpeedText.setText(Float.toString(maxSpeed));
		
		// printing distance
		Location origin = mLocList.get(0);
		Location dest = mLocList.get(mLocList.size());
		double distance = Distance(origin, dest);
		distanceText.setText(Double.toString(distance));
		
		cbStraightLine.setChecked(StraightLine3D(mLocList));
	}

}
