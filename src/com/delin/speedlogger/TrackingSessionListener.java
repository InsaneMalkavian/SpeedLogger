package com.delin.speedlogger;

import java.util.List;

import com.delin.speedlogger.TrackingSession.WarmupState;

import android.location.Location;

public interface TrackingSessionListener {
	void onSessionWarmingUp(WarmupState mWarmupState);
	void onSessionReady();
	void onSessionStart();
	void onSessionFinished(List<Location> mLocList);
	void onSessionError();
	void onSessionLocationUpdate(Location location);
	void onSessionStopped();
}
