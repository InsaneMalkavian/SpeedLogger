package com.delin.speedlogger.Utils;

public class Converter {
	private static final float MS_KMPH = 3.6f;
	private static final float MPH_KMPH = 1.60934f;
	
	// meteres/sec --> kilometers/hour 
	public static final float ms2kph(float speed) {
		return speed * MS_KMPH;
	}
	
	// kilometers/hour --> meteres/sec 
	public static final float kph2ms(float speed) {
		return speed / MS_KMPH;
	}

	public static final float kph2mhp(float speed) {
		return speed / MPH_KMPH;
	}

	public static final float ms2mhp(float speed) {
		return speed * MS_KMPH / MPH_KMPH;
	}
}
