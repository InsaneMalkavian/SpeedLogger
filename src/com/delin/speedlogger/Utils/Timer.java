package com.delin.speedlogger.Utils;

import android.os.Handler;

/** 
 * 	based on: 
 * 		http://stackoverflow.com/questions/6242268/repeat-a-task-with-a-time-delay/6242292#6242292
 * 	because of:
 * 		http://jayxie.com/mirrors/android-sdk/resources/articles/timed-ui-updates.html
 * 		http://www.mopri.de/2010/timertask-bad-do-it-the-android-way-use-a-handler/
 */

public class Timer {
	private Handler mHandler = new Handler();
    private Runnable mStatusChecker;
    public long mInterval;

    public Timer(final Runnable task){
        mStatusChecker = new Runnable() {
            @Override
            public void run() {
                task.run();
                mHandler.postDelayed(this, mInterval);
            }
        };
    }

    public void start(long interval){
    	mInterval = interval;
        mStatusChecker.run();
    }

    public void stop(){
        mHandler.removeCallbacks(mStatusChecker);
    }
}
