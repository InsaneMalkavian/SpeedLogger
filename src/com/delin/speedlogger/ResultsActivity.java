package com.delin.speedlogger;

import java.util.List;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

public class ResultsActivity extends Activity {
	TextView mMaxSpeed;
	TextView mDistance;
	CheckBox mStraightLine;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);
		mMaxSpeed = (TextView)findViewById(R.id.textDistance);
		mDistance = (TextView)findViewById(R.id.textMaxSpeed);
	}
	
	// TODO: Fail. This should return distance in meters :D
	//       and now returns whatever this is
	public double Distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
	}
	
	// TODO: it must be deleted or rewritten
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
	
	public boolean StraightLine2D(List<Location> locList) {
		// TODO: Delin's suggestion is to use second point as origin
		Location origin = locList.get(0);
		Location dest = locList.get(locList.size()-1);
		
		// first point
		double a_x = origin.getLatitude();
		double a_y = origin.getLongitude();
		// last point
		double b_x = dest.getLatitude();
		double b_y = dest.getLongitude();
		
		// TODO: 2 better be replaced with something
		if (locList.size() <= 2 || Distance(a_x, a_y, b_x, b_y) < 50)
			// for such a small path distance we assume it is a straight line
			return true;
		else {	
			// line formula
			double k = (a_y - b_y) / (a_x - b_x);
		    double b = a_y - k * a_x;
			
			// TODO: get proper number for eps
		    // eps is a max allowed distance between real point 
		    // and a 'perfect' point to suit the line (in meters)
			double eps = 5.d;
			for (int i=2; i<locList.size(); i++){
				double x = locList.get(i).getLatitude();
				double y = locList.get(i).getLongitude();

				// calculating what y should be for this x 
				// if point lied exactly on the line
				double y1 = b + k * a_x;
				
				// this is a distance between our real point 
				// and 'perfect' point to suit the line
				if (Distance(x,y,x,y1) > eps)
					return false;
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
		mMaxSpeed.setText(Float.toString(maxSpeed));
		
		// printing distance
		Location origin = mLocList.get(0);
		Location dest = mLocList.get(mLocList.size());
		double distance = Distance(origin.getLatitude(), origin.getLongitude(), 
				                   dest.getLatitude(), dest.getLongitude());
		mDistance.setText(Double.toString(distance));
		
		mStraightLine.setChecked(StraightLine2D(mLocList));
	}

}
