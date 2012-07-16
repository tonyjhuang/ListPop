package com.tonyjhuang.listpop;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DbAdapter {

	
	
	public static final String KEY_ROWID="_id";
	public static final String KEY_LIST_HEADER_COLUMN="List_Header";
	public static final String KEY_LIST_COLUMN="List";
	
	private ListPopDBOpenHelper mDbHelper;
	private SQLiteDatabase mDb;

	
	
	private final Context mCtx;
	
	public DbAdapter(Context ctx) {
		this.mCtx = ctx;
	}
	
	//Opens Read/Write-accessible database.
	//Call this in onCreate, onResume!
	public DbAdapter open() throws android.database.SQLException{
		mDbHelper = new ListPopDBOpenHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}
	
	//Closes database to free up resources.
	//Call this in onStop!
	public void close(){
		mDbHelper.close();
	}

	
	
	//Insert ArrayList as flattened JSON object
	public long enterList(String list_header, ArrayList<String> array) throws JSONException {
		 JSONObject json = new JSONObject();
		 json.put("array", new JSONArray(array)); 
		 // ArrayList of strings converted to JSON object, flattened to string (arrayList)
		 String arrayList = json.toString();
		
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_LIST_HEADER_COLUMN,	list_header);
		initialValues.put(KEY_LIST_COLUMN, arrayList);	
		
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}
	
	public boolean deleteById(long id){
		return mDb.delete(DATABASE_TABLE,  KEY_ROWID+"="+id, null) > 0;
	}
	
	public void deleteAll(){
		 mDb.delete(DATABASE_TABLE, null, null);
	}
	
	public Cursor fetchByRowId(long rowId) throws SQLException {
		Cursor mCursor =
				mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,KEY_LIST_HEADER_COLUMN,KEY_LIST_COLUMN},
						KEY_ROWID+"="+rowId, null, null, null, null, null);
		if (mCursor != null){
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	//Returns a cursor with all columns' row id's and byte arrays.
	public Cursor fetchAll(){
		Cursor mCursor = mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_LIST_HEADER_COLUMN,KEY_LIST_COLUMN},
				null, null, null, null, null);
		if (mCursor != null){
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public Cursor debugFetch(){
		return fetchAll();
	}
	
	public long fetchNumberOfLists(){
		String cmd = "SELECT COUNT(*) FROM " + DATABASE_TABLE;
		SQLiteStatement statement = mDb.compileStatement(cmd);
		long count = statement.simpleQueryForLong();
		return count;
	}	
	
	public boolean isEmpty(){
		return fetchNumberOfLists() == 0;
	}

	private static class ListPopDBOpenHelper extends SQLiteOpenHelper {
		private static final String DATABASE_NAME="ListPopDB.db";
		private static final String DATABASE_TABLE="CacheOfLists";
		private static final int DATABASE_VERSION=1;
		
		private static final String DATABASE_CREATE=
				"create table " + DATABASE_TABLE + " ("  
					+ KEY_ROWID + " integer primary key autoincrement, "
					+ KEY_LIST_HEADER_COLUMN + " text not null, "
					+ KEY_LIST_COLUMN + " text not null);";
		
		ListPopDBOpenHelper(Context c, String name,
				CursorFactory factory, int version){
			super(c, name, factory, version);
		}
		
		//Called when no database exists on disk and the helper class needs
		// to create a new one.
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}
		
		//Callaed when there is a database version mismatch meaning that
		// the current version of the database on disk needs to be
		// upgraded to the newest version.
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion,
				int newVersion){
			//Log the version upgrade.
			Log.w("ListDbAdapter", "Upgrading from version " + 
					oldVersion + " to " + 
					newVersion + ", which will destroy all old data");
			
			//Upgrade the existing database to conform to the new
			// version. Multiple 
		}
		
	}
	
}

