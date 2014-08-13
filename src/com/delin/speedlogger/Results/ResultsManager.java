package com.delin.speedlogger.Results;

import java.util.ArrayList;
import java.util.List;

import com.delin.speedlogger.Serialization.DBManager;

import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;

public class ResultsManager {
    List<SessionResult> results = new ArrayList<SessionResult>();
    private Context mContext;
    private DBManager database;

    private static final ResultsManager instance = new ResultsManager();
    private ResultsManager() {}

    public void init(Context context) {
        mContext = context;
        //SQLiteDatabase.loadLibs(mContext);
        database = new DBManager(mContext);
        LoadData();
    }

    public static ResultsManager getInstance() {
        return instance;
    }

    public void addResult(SessionResult result) {
        results.add(result);
        database.InsertSessionResult(result);
        result.SaveGPX();
    }

    public List<SessionResult> getResults() {
        return results;
    }

    private void LoadData() {
        results = database.GetSessionResults();
        // if database is empty use hardcoded values
        // TODO: delete after debug
        if(results.isEmpty()) LoadDummyData();
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

    public void clearLocalResults() {
        database.ClearLocalResults();
        for(SessionResult r : results) r.DeleteGPX();
        results.clear();
    }
}
