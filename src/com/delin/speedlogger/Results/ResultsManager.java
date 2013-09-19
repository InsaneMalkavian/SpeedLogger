package com.delin.speedlogger.Results;

import java.util.ArrayList;
import java.util.List;

import com.delin.speedlogger.Serialization.DBManager;

import net.sqlcipher.database.SQLiteDatabase;

import android.content.Context;

public class ResultsManager {
	List<StoredRecord> mResults = new ArrayList<StoredRecord>();
	StoredRecord mSelectedResult;
	private Context mContext;
	private DBManager mDatabase;
	
	private static final ResultsManager instance = new ResultsManager();    
	private ResultsManager() {}
	
	public void Init(Context context) {
		mContext = context;
		SQLiteDatabase.loadLibs(mContext);
		mDatabase = new DBManager(mContext);
		LoadData();
	}
	
	public static ResultsManager GetInstance() {
		return instance;
	}
	
	public void AddResult(StoredRecord result) {
		mResults.add(result);
		mDatabase.InsertResult(result);
	}
	
	public List<StoredRecord> GetResults() {
		return mResults;
	}
	
	private void LoadData() {	
		mResults = mDatabase.GetResults();
		// if database is empty use hardcoded values
		// TODO: delete after debug
		if(mResults.isEmpty()) LoadDummyData();
	}
		
	private void LoadDummyData() {
		/*SessionResult result = new SessionResult();
		result.setStartTime(1000); // just random values
		result.setMaxSpeed(150);
		result.setDistance(200);
		result.setDuration(50);
		AddResult(result);
		
		result = new SessionResult();
		result.setStartTime(2000);
		result.setMaxSpeed(100);
		result.setDistance(300);
		result.setDuration(100);
		AddResult(result);
		
		result = new SessionResult();
		result.setStartTime(1500);
		result.setMaxSpeed(250);
		result.setDistance(400);
		result.setDuration(150);
		AddResult(result);*/
	}
	
	public void ClearLocalResults() {
		mDatabase.ClearLocalResults();
		mResults.clear();
	}
	
	public void SetSelectedResult(StoredRecord result){
		mSelectedResult = result;
	}
	
	public StoredRecord GetSelectedResult(){
		return mSelectedResult;
	}

}
