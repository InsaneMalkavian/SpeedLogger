package com.delin.speedlogger;

import java.util.List;

import android.location.Location;
import android.util.Log;

// TODO: use Point class instead of ax,ay,az
public class Geometry {
	public static double sqrDistPointToPoint3D(double ax, double ay, double az, double bx, double by, double bz){
	    return (ax-bx)*(ax-bx) + (ay-by)*(ay-by) + (az-bz)*(az-bz);
	}
	
	// got it here: http://krasprog.ru/persons.php?page=demidenko&blog=10
	public static double distPointToLine(double Px, double Py, double Pz, double Ax, double Ay, double Az, 
						                 double Bx, double By, double Bz)
	{
	    double pa, pb, ab; // squares of distances PA, PB, AB
	    pa = sqrDistPointToPoint3D(Px,Py,Pz,Ax,Ay,Az);
	    pb = sqrDistPointToPoint3D(Px,Py,Pz,Bx,By,Bz);
	    ab = sqrDistPointToPoint3D(Ax,Ay,Az,Bx,By,Bz);
	    
	    // if perpendicular is out of line then closest point is one of line edges
	    if(pa >= pb + ab) return Math.sqrt(pb);
	    if(pb >= pa + ab) return Math.sqrt(pa);
	    
	    double a1, a2, a3, b1, b2, b3; // temp vars
	    a1=Ax-Px; a2=Ay-Py; a3=Az-Pz;
	    b1=Bx-Px; b2=By-Py; b3=Bz-Pz;
	    
	    return Math.sqrt(sqrDistPointToPoint3D(a2*b3-a3*b2,-(a1*b3-a3*b1),a1*b2-b1*a2,0,0,0)/ab);
	}
	
	// HINT: this is generalized version for both 2D and 3D.
	//		 you can make separate func for 2D case to make simpler calculus there
	public static boolean StraightLine(List<Location> locList, boolean use3D){
		// for a small path we assume it is a straight line
		// TODO: 2 better be replaced with something
		if (locList.size() <= 2) return true;
		
		//Location origin = locList.get(0);
		Location origin = locList.get(1);
		Location dest = locList.get(locList.size()-1);
			
		// lat/lon to UTM convert is necessary to perform line calculus
		// UTM values represented in meters
		// altitude's already in meters, no conversion needed
		double x1 = Mercator.mercX(origin.getLongitude());
		double y1 = Mercator.mercY(origin.getLatitude());
		double z1 = use3D? origin.getAltitude() : 0;
		double x2 = Mercator.mercX(dest.getLongitude());
		double y2 = Mercator.mercY(dest.getLatitude());
		double z2 = use3D? dest.getAltitude() : 0;
		
		// TODO: get proper number for eps
		// TODO: move eps to the place where constants live
		double eps = 25.d; // max allowed deviation from the line (meters)
		for (int i=1; i<locList.size(); i++){
			Location loc = locList.get(i);
			double x = Mercator.mercX(loc.getLongitude());
			double y = Mercator.mercY(loc.getLatitude());
			double z = use3D? loc.getAltitude() : 0;
			double d = distPointToLine(x,y,z,x1,y1,z1,x2,y2,z2);
			
			Log.i("Geometry","x = " + Double.toString(x) + 
				  "  y = " + Double.toString(y) +  "  z = " + 
				  Double.toString(z) +  "  d = " + Double.toString(d));
			
			if (d > eps) return false;
		}
		return true;
	}
	
	public static double DistBetweenLocs(Location loc1, Location loc2, boolean use3D){
		double x1 = Mercator.mercX(loc1.getLatitude ());
		double y1 = Mercator.mercY(loc1.getLongitude());
		double z1 = use3D? loc1.getAltitude() : 0;
		double x2 = Mercator.mercX(loc2.getLatitude());
		double y2 = Mercator.mercY(loc2.getLongitude ());
		double z2 = use3D? loc2.getAltitude() : 0;
		
		return Math.sqrt(sqrDistPointToPoint3D(x1,y1,z1,x2,y2,z2));
	}
}
