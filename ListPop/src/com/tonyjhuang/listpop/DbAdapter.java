package com.tonyjhuang.listpop;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DbAdapter {

	private static final String DATABASE_NAME="p1";
	private static final String DATABASE_TABLE="lines";
	private static final int DATABASE_VERSION=1;
	
	public static final String KEY_ROWID="_id";
	public static final String KEY_PHRASES="phrases";
	
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_CREATE=
			"create table " + DATABASE_TABLE + " ("  
				+ KEY_ROWID + " integer primary key autoincrement, "
				+ KEY_PHRASES + " text not null);";
	
	private final Context mCtx;
	
	public DbAdapter(Context ctx) {
		this.mCtx = ctx;
	}
	
	public DbAdapter open() throws android.database.SQLException{
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		mDbHelper.close();
	}

	public long enterPhrase(String i) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_PHRASES, i);
		
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}
	
	//Delete's the first entry to the database (first in first out)
	public void deleteFirst(){
		Cursor c = mDb.query(DATABASE_TABLE, null, null, null, null, null, null);
			
		if(c.moveToFirst()) {
			long rowId = c.getLong(c.getColumnIndex(KEY_ROWID));
				
			mDb.delete(DATABASE_TABLE, KEY_ROWID+"="+rowId, null);
		}
	}
		
	//Delete's the last entry to the database (first in last out)
	public void deleteLast(){
		mDb.execSQL("DELETE FROM "+DATABASE_TABLE+" WHERE "+KEY_ROWID+
				" = (SELECT MAX("+KEY_ROWID+") FROM "+DATABASE_TABLE+");");
	}
	
	public void deleteByName(String phrase){
		 mDb.delete(DATABASE_TABLE, "=?", new String[] { phrase });
	}
	
	public boolean deleteById(long id){
		return mDb.delete(DATABASE_TABLE,  KEY_ROWID+"="+id, null) > 0;
	}
	
	public void deleteAll(){
		 mDb.delete(DATABASE_TABLE, null, null);
		
	}

	public long fetchRowCount(){
		String cmd = "SELECT COUNT(*) FROM " + DATABASE_TABLE;
		SQLiteStatement statement = mDb.compileStatement(cmd);
		long count = statement.simpleQueryForLong();
		return count;
	}
	
	
	public Cursor fetchRandomRow() {
		if (fetchRowCount()>0){
		SQLiteStatement s = mDb.compileStatement("SELECT * FROM "+DATABASE_TABLE+" ORDER BY RANDOM() LIMIT 1;");
		Cursor c = fetchByRowId(Long.valueOf(s.simpleQueryForString()));
		return c;
		} else {
			return null;
		}
		
	}
	
	public Cursor fetchByRowId(long rowId) throws SQLException {
		Cursor mCursor =
				mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,KEY_PHRASES},
						KEY_ROWID+"="+rowId, null, null, null, null, null);
		if (mCursor != null){
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	//returns a result set of all existing phrases and their rowid's in the db
	public Cursor fetchAll(){
		return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_PHRASES},
				null, null, null, null, null);
	}
		
	public boolean isActionable(){
		return fetchRowCount() > 0;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context c){
			super(c, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion,
				int newVersion){
			//Not used
		}
		
	}
	
}

