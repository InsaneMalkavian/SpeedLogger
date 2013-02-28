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
	
	// got it here: http://wiki.openstreetmap.org/wiki/Mercator#Java_Implementation
	public double mercX(double lon){
		double r_big = 6378137.0; // big radius of an ellipse
		return r_big * Math.toRadians(lon);
	}
	// got it here: http://wiki.openstreetmap.org/wiki/Mercator#Java_Implementation
	public double mercY(double lat) {
		double r_big = 6378137.0; // big radius of an ellipse
		double r_small = 6356752.3142; // small radius of an ellipse
        if (lat > 89.5) {
            lat = 89.5;
        }
        if (lat < -89.5) {
            lat = -89.5;
        }
        double temp = r_big / r_small;
        double es = 1.0 - (temp * temp);
        double eccent = Math.sqrt(es);
        double phi = Math.toRadians(lat);
        double con = eccent * Math.sin(phi);
        double com = 0.5 * eccent;
        con = Math.pow(((1.0-con)/(1.0+con)), com);
        double ts = Math.tan(0.5 * ((0.5*Math.PI) - phi))/con;
        double y = 0 - r_big * Math.log(ts);
        return y;
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
		Location origin = locList.get(0);
		Location dest = locList.get(locList.size()-1);
		
		// lat/lon to UTM convert is necessary to perform line calculus
		// values represented in meters
		double x1 = mercX(origin.getLongitude());
		double y1 = mercY(origin.getLatitude());
		double x2 = mercX(dest.getLongitude());
		double y2 = mercY(dest.getLatitude());
		
		// TODO: 2 better be replaced with something
		if (locList.size() <= 2 || Distance(x1, y1, x2, y2) < 50)
			return true; // for such a small path we assume it is a straight line
		else {	
			// line formula Ax + By + C = 0
			double A = y1 - y2;
		    double B = x2 - x1;
		    double C = x1*y2 - x2*y1;
			
		    // TODO: get proper number for eps
		    // TODO: move eps to the place where constants live
			double eps = 5.d; // max allowed deviation from the line (meters)
			for (int i=2; i<locList.size(); i++){
				Location loc = locList.get(i);
				double x = mercX(loc.getLongitude());
				double y = mercY(loc.getLatitude());
				double d = (A*x+B*y+C)/Math.sqrt(A*A+B*B); // distance from point to line
				if (d > eps)
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
