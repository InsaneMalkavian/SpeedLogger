package com.delin.speedlogger;

import java.util.ArrayList;
import java.util.List;

public class ResultsManager {
	List<SessionResult> mResults = new ArrayList<SessionResult>();
	private static final ResultsManager instance = new ResultsManager();    
  
	private ResultsManager() {
		LoadData();
	}
	
	 public static ResultsManager GetInstance() {
	        return instance;
	    }
	
	public void AddResult(SessionResult result) {
		mResults.add(result);
		// TODO: serialize added result
	}
	
	public List<SessionResult> GetResults() {
		return mResults;
	}
	
	public void LoadData() {
		// TODO: load persistent data; now here is hardcoded replacement
		SessionResult result = new SessionResult();
		result.mStartTime = 1000; // just random values
		result.mMaxSpeed = 150;
		result.mDistance = 200;
		result.mDuration = 50;
		mResults.add(result);
		
		result = new SessionResult();
		result.mStartTime = 2000;
		result.mMaxSpeed = 100;
		result.mDistance = 300;
		result.mDuration = 100;
		mResults.add(result);
		
		result = new SessionResult();
		result.mStartTime = 1500;
		result.mMaxSpeed = 250;
		result.mDistance = 400;
		result.mDuration = 150;
		mResults.add(result);
	}

}
