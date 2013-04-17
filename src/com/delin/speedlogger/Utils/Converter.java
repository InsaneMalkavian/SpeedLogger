package com.delin.speedlogger.Utils;

public class Converter {
	// meteres/sec --> kilometers/hour 
	public static final float ms2kph(float speed) {
		return (float) (speed * 3.6);
	}
	
	// kilometers/hour --> meteres/sec 
	public static final float kph2ms(float speed) {
		return (float) (speed / 3.6);
	}
}
