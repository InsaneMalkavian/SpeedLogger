package com.delin.speedlogger.Serialization;

import java.util.ArrayList;
import java.util.List;

import com.delin.speedlogger.Results.StoredRecord;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper {
	private static final String DATABASE_NAME 		= "SpeedLogger.db"; // TODO: get app name from strings.xml or something
	private static final String DATABASE_PASSWORD 	= "Push! Push! Push!";
	private static final int 	DATABASE_VERSION 	= 2;
	
	//------------------- LOCAL_RESULTS table------------------------
	private static final String LOCAL_RESULTS 		= "LOCAL_RESULTS";
	// columns: session info
	private static final String START_TIME 	 		= "START_TIME";
	private static final String MAX_SPEED 	 		= "MAX_SPEED";
	private static final String TOTAL_DISTANCE 	 	= "TOTAL_DISTANCE";
	private static final String TOTAL_TIME 	 		= "TOTAL_TIME";
	// columns: car info
	private static final String CAR_BRAND 	 		= "CAR_BRAND";
	private static final String CAR_MODEL 	 		= "CAR_MODEL";
	private static final String CAR_MODEL_INDEX 	= "CAR_MODEL_INDEX";
	private static final String CAR_GEARBOX 	 	= "CAR_GEARBOX";
	private static final String CAR_HORSE_POWER		= "CAR_HORSE_POWER";
	private static final String CAR_TORQUE	 		= "CAR_TORQUE";
	
		
	public DBManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table " + LOCAL_RESULTS +
						"( " +
							BaseColumns._ID + " integer primary key autoincrement, " +
							START_TIME 		+ " integer not null, " +
							MAX_SPEED 		+ " real 	not null, " +
							TOTAL_DISTANCE 	+ " real 	not null, " +
							TOTAL_TIME 		+ " integer not null, " +
							CAR_BRAND 	 	+ " string 	not null, " +
							CAR_MODEL 	 	+ " string 	not null, " +
							CAR_MODEL_INDEX + " string 	not null, " +
							CAR_GEARBOX 	+ " string 	not null, " +
							CAR_HORSE_POWER	+ " integer not null, " +
							CAR_TORQUE	 	+ " integer not null  " +
						");";
		db.execSQL(sql);
		Log.i("DBManager", "onCreate(): " + sql);
	}
	
	public void InsertResult(StoredRecord result) {
	    ContentValues values = new ContentValues();
	    
	    values.put(START_TIME, 		result.GetStartTime());
	    values.put(MAX_SPEED, 		result.GetMaxSpeed());
	    values.put(TOTAL_DISTANCE, 	result.GetTotalDistance());
	    values.put(TOTAL_TIME, 		result.GetTotalTime());
//	    values.put(CAR_BRAND, 		result.GetCar().GetBrand());
//	    values.put(CAR_MODEL, 		result.GetCar().GetModel());
//	    values.put(CAR_MODEL_INDEX, result.GetCar().GetModelIndex());
//	    values.put(CAR_GEARBOX, 	result.GetCar().GetGearbox());
//	    values.put(CAR_HORSE_POWER, result.GetCar().GetHorsePower());
//	    values.put(CAR_TORQUE, 		result.GetCar().GetTorque());
	    values.put(CAR_BRAND, 		"");
	    values.put(CAR_MODEL, 		"");
	    values.put(CAR_MODEL_INDEX, "");
	    values.put(CAR_GEARBOX, 	"");
	    values.put(CAR_HORSE_POWER, "");
	    values.put(CAR_TORQUE, 		"");
	    
	    SQLiteDatabase db = getWritableDatabase(DATABASE_PASSWORD);
	    db.insert(LOCAL_RESULTS, null, values);
	    db.close();
	    
	    Log.i("DBManager", "InsertSessionResult()");
	  }
	
	public List<StoredRecord> GetResults() {
		// load data from DB
		SQLiteDatabase db = getReadableDatabase(DATABASE_PASSWORD);
	    Cursor cursor = db.query(LOCAL_RESULTS,null,null,null,null,null,null);
	    // parse data
	    List<StoredRecord> results = new ArrayList<StoredRecord>();
	    while(cursor.moveToNext()){
	    	StoredRecord r = new StoredRecord();
			
	    	// start from 1 because 0 is for id_ column
	    	r.SetStartTime				(cursor.getLong		(1));
			r.SetMaxSpeed				(cursor.getFloat	(2));
			r.SetTotalDistance			(cursor.getFloat	(3));
			r.SetTotalTime				(cursor.getLong		(4));
			r.GetCar().SetBrand			(cursor.getString	(5));
			r.GetCar().SetModel			(cursor.getString	(6));
			r.GetCar().SetModelIndex	(cursor.getString	(7));
			r.GetCar().SetGearbox		(cursor.getString	(8));
			r.GetCar().SetHorsePower	(cursor.getLong		(9));
			r.GetCar().SetTorque		(cursor.getLong		(10));
			
			results.add(r);
	      }
	    cursor.close(); // TODO: can be buggy. read about managing cursors
	    db.close();
	    Log.i("DBManager", "GetSessionResults()");
	    return results;
	}
	
	public void ClearLocalResults() {
		String sql = "delete from " + LOCAL_RESULTS;
		SQLiteDatabase db = getWritableDatabase(DATABASE_PASSWORD);
		db.execSQL(sql);
		db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("onUpgrade", "oldVersion: " + oldVersion + "  newVersion: " + newVersion);
		// later we'll try to transform DB and save old data here;
		// currently it deletes old DB and creates a new one
		db.execSQL("DROP TABLE IF EXISTS "+ LOCAL_RESULTS);
		onCreate(db);
	}

}
