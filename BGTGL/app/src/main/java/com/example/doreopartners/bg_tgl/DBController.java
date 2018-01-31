package com.example.doreopartners.bg_tgl;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DBController  extends SQLiteOpenHelper {

	public DBController(Context applicationcontext) {
        super(applicationcontext, "androidsqlite.db", null, 1);
    }
	//Creates Table
	@Override
	public void onCreate(SQLiteDatabase database) {
		String query;
		query = "CREATE TABLE tgl ( userId INTEGER PRIMARY KEY, userName TEXT, udpateStatus TEXT)";
        database.execSQL(query);
	}
	@Override
	public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
		String query;
		query = "DROP TABLE IF EXISTS tgl";
		database.execSQL(query);
        onCreate(database);
	}
	/**
	 * Inserts User into SQLite DB
	 * @param queryValues
	 */
	public void insertUser(HashMap<String, String> queryValues) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("userName", queryValues.get("userName"));
		values.put("udpateStatus", "no");
		database.insert("users", null, values);
		database.close();
	}
	
	/**
	 * Get list of Users from SQLite DB as Array List
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getAllUsers() {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM tgl";
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	        do {
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	map.put("id", cursor.getString(0));
	        	map.put("ik_number", cursor.getString(1));
				map.put("tgl_name", cursor.getString(2));
				map.put("qus1", cursor.getString(3));
				map.put("qus2", cursor.getString(4));
				map.put("qus3", cursor.getString(5));
				map.put("qus4", cursor.getString(6));
				map.put("qus5", cursor.getString(7));
				map.put("qus6", cursor.getString(8));
				map.put("qus7", cursor.getString(9));
				map.put("qus8", cursor.getString(10));
				map.put("qus9", cursor.getString(11));
				map.put("qus10", cursor.getString(12));
				map.put("qus11", cursor.getString(13));
				map.put("qus12", cursor.getString(14));
				map.put("score", cursor.getString(15));
				map.put("interviewer", cursor.getString(16));
				map.put("date", cursor.getString(17));
				map.put("status", cursor.getString(18));

                wordList.add(map);
	        } while (cursor.moveToNext());
	    }
	    database.close();
	    return wordList;
	}
	
	/**
	 * Compose JSON out of SQLite records
	 * @return
	 */
	public String composeJSONfromSQLite(){
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM tgl where status = '"+"0"+"'";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("id", cursor.getString(0));
				map.put("ik_number", cursor.getString(1));
				map.put("tgl_name", cursor.getString(2));
				map.put("qus1", cursor.getString(3));
				map.put("qus2", cursor.getString(4));
				map.put("qus3", cursor.getString(5));
				map.put("qus4", cursor.getString(6));
				map.put("qus5", cursor.getString(7));
				map.put("qus6", cursor.getString(8));
				map.put("qus7", cursor.getString(9));
				map.put("qus8", cursor.getString(10));
				map.put("qus9", cursor.getString(11));
				map.put("qus10", cursor.getString(12));
				map.put("qus11", cursor.getString(13));
				map.put("qus12", cursor.getString(14));
				map.put("score", cursor.getString(15));
				map.put("interviewer", cursor.getString(16));
				map.put("date", cursor.getString(17));
				map.put("status", cursor.getString(18));
				wordList.add(map);
			} while (cursor.moveToNext());
		}
		database.close();
		Gson gson = new GsonBuilder().create();
		//Use GSON to serialize Array List to JSON
		return gson.toJson(wordList);
	}
	
	/**
	 * Get Sync status of SQLite
	 * @return
	 */
	public String getSyncStatus(){
	    String msg = null;
	    if(this.dbSyncCount() == 0){
	    	msg = "SQLite and Remote MySQL DBs are in Sync!";
	    }else{
	    	msg = "DB Sync needed\n";
	    }
	    return msg;
	}
	
	/**
	 * Get SQLite records that are yet to be Synced
	 * @return
	 */
	public int dbSyncCount(){
		int count = 0;
		String selectQuery = "SELECT  * FROM tgl where status = '"+"0"+"'";
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    count = cursor.getCount();
	    database.close();
		return count;
	}
	
	/**
	 * Update Sync status against each User ID
	 * @param id
	 * @param status
	 */
	public void updateSyncStatus(String id, String status){
		SQLiteDatabase database = this.getWritableDatabase();	 
		String updateQuery = "Update tgl set status = '"+ status +"' where _id="+"'"+ id +"'";
		Log.d("query",updateQuery);		
		database.execSQL(updateQuery);
		database.close();
	}
}
