package com.tonyjhuang.listpop;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;




public class DbAdapter {

	private static final String DATABASE_NAME="ListPopDB.db";
	private static final String DATABASE_TABLE="CacheOfLists";
	private static final int DATABASE_VERSION=1;
	
	public static final String KEY_ROWID="_id";
	public static final String KEY_LIST_HEADER_COLUMN="List_Header";
	public static final String KEY_LIST_COLUMN="List";
	
	private ListPopDBOpenHelper mListPopDBOpenHelper;
	private SQLiteDatabase mDb;
	
	private final Context mCtx;
	
	public DbAdapter(Context ctx) {
		this.mCtx = ctx;
	}
	
	//Opens Read/Write-accessible database.
	//Call this in onCreate, onResume!
	public DbAdapter open() throws android.database.SQLException{
		mListPopDBOpenHelper = new ListPopDBOpenHelper(mCtx, DATABASE_NAME, 
														null, DATABASE_VERSION);
		mDb = mListPopDBOpenHelper.getWritableDatabase();
		return this;
	}
	
	//Closes database to free up resources.
	//Call this in onStop!
	public void close(){
		mListPopDBOpenHelper.close();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public long enterList(String list_header, ArrayList<String> array) throws FileNotFoundException, IOException{
		ContentValues values = new ContentValues();
		values.put(KEY_LIST_HEADER_COLUMN, list_header);
		values.put(KEY_LIST_COLUMN, toString(array));	
		
		return mDb.insert(DATABASE_TABLE, null, values);
	}
	
	
	
	 //Write the object to a Base64 string. 
    private static String toString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return new String( Base64.encode( baos.toByteArray(), 0 ) );
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
	
	public long fetchNumberOfLists(){
		return fetchAll().getCount();
	}	

	private static class ListPopDBOpenHelper extends SQLiteOpenHelper {
		
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
		
		//Called when there is a database version mismatch meaning that
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

