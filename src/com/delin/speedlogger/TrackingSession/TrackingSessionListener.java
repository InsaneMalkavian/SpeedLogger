package com.delin.speedlogger.TrackingSession;

import java.util.List;

import com.delin.speedlogger.TrackingSession.TrackingSession.WarmupState;

import android.location.Location;

public interface TrackingSessionListener {
	void onSessionWarmingUp(WarmupState mWarmupState);
	void onSessionReady();
	
	/**
	 * Called on start of tracking session
	 *
	 * @param  startTime  time at which session has been started, in milliseconds since 01.01.1970 UTC
	 */
	void onSessionStart(long startTime);
	void onSessionFinished(List<Location> locList);
	void onSessionError();
	void onSessionLocationUpdate(Location location);
	void onSessionStopped();
}
