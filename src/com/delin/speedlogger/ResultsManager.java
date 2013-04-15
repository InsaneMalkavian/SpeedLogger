package com.delin.speedlogger;

import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;

import android.content.Context;

public class ResultsManager {
	List<SessionResult> mResults = new ArrayList<SessionResult>();
	private Context mContext;
	private DBManager db;
	
	private static final ResultsManager instance = new ResultsManager();    
	private ResultsManager() {}
	
	public void Init(Context context) {
		mContext = context;
		SQLiteDatabase.loadLibs(mContext);
		db = new DBManager(mContext);
		LoadData();
	}
	
	public static ResultsManager GetInstance() {
		return instance;
	}
	
	public void AddResult(SessionResult result) {
		mResults.add(result);
		db.InsertSessionResult(result);
	}
	
	public List<SessionResult> GetResults() {
		return mResults;
	}
	
	private void LoadData() {	
		mResults = db.GetSessionResults();
		// if database is empty use hardcoded values
		// TODO: delete after debug
		if(mResults.isEmpty()) LoadDummyData();
	}
		
	private void LoadDummyData() {
		SessionResult result = new SessionResult();
		result.mStartTime = 1000; // just random values
		result.mMaxSpeed = 150;
		result.mDistance = 200;
		result.mDuration = 50;
		AddResult(result);
		
		result = new SessionResult();
		result.mStartTime = 2000;
		result.mMaxSpeed = 100;
		result.mDistance = 300;
		result.mDuration = 100;
		AddResult(result);
		
		result = new SessionResult();
		result.mStartTime = 1500;
		result.mMaxSpeed = 250;
		result.mDistance = 400;
		result.mDuration = 150;
		AddResult(result);
	}

}
