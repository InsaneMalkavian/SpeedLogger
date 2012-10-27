package com.delin.speedlogger;

import android.location.Location;

public interface TrackingSessionListener {
	void onSessionWarmingUp();
	void onSessionReady();
	void onSessionStart();
	void onSessionFinished(Location[] mLocArray);
	void onSessionError();
	void onSessionLocationUpdate(Location location);
	void onSessionStopped();
}
