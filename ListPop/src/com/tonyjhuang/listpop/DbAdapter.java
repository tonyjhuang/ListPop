package com.tonyjhuang.listpop;

import java.util.ArrayList;
import java.util.Collections;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {

	private static final String DATABASE_NAME = "ListPopDB.db";
	private static final String DATABASE_TABLE = "CacheOfLists";
	private static final int DATABASE_VERSION = 1;

	public static final String ROWID = "_id";
	public static final String LIST_HEADER = "List_Header";
	public static final String LIST = "List";

	private ListPopDBOpenHelper mListPopDBOpenHelper;
	private SQLiteDatabase mDb;

	private final Context mCtx;

	public DbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	// Opens Read/Write-accessible database.
	// Call this in onCreate in the main activity!
	public DbAdapter open() throws android.database.SQLException {
		mListPopDBOpenHelper = new ListPopDBOpenHelper(mCtx, DATABASE_NAME,
				null, DATABASE_VERSION);
		mDb = mListPopDBOpenHelper.getWritableDatabase();
		return this;
	}

	// Closes database to free up resources.
	// Call this in onDestroy only!
	public void close() {
		mListPopDBOpenHelper.close();
	}

	// Enters a row into the database with a String as the list name and
	// a String as the ArrayList. See: arraylistToString().
	public long enterList(String list_header, ArrayList<String> array) {
		String sArray = arraylistToString(array);

		ContentValues values = new ContentValues();
		values.put(LIST_HEADER, list_header);
		values.put(LIST, sArray);

		return mDb.insert(DATABASE_TABLE, null, values);
	}

	// Converts an ArrayList of Strings into a single string to
	// store in the database. Used to avoid serializing ArrayLists.
	private String arraylistToString(ArrayList<String> arraylist) {
		String s = "";
		Collections.reverse(arraylist);
		for (String i : arraylist) {
			s = i + "|" + s;
		}
		return s;
	}

	// Deletes a single row in the database and returns a boolean
	// representing whether the transaction was successful or not.
	public boolean deleteListItem(long id) {
		return mDb.delete(DATABASE_TABLE, ROWID + "=" + id, null) > 0;
	}

	// Deletes all rows in database. Has no usage now but will probably
	// be helpful in future debugging...
	public void deleteAll() {
		mDb.delete(DATABASE_TABLE, null, null);
	}

	// Returns a result set containing one row.
	public Cursor fetchListItem(long rowId) throws SQLException {
		Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] { ROWID,
				LIST_HEADER, LIST }, ROWID + "=" + rowId, null, null, null,
				null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// Returns a result set containing all rows in the database.
	public Cursor fetchAll() {
		return mDb.query(DATABASE_TABLE, new String[] { ROWID, LIST_HEADER,
				LIST }, null, null, null, null, null);
	}

	// Returns true if there are no rows in the database.
	public boolean isEmpty() {
		return fetchAll().getCount() == 0;
	}

	// Implementation of SQLiteOpen Helper. Creates database when needed and
	// aids in rolling out database upgrades. This functionality is
	// probably not needed though.
	private static class ListPopDBOpenHelper extends SQLiteOpenHelper {

		private static final String DATABASE_CREATE = "create table "
				+ DATABASE_TABLE + " (" + ROWID
				+ " integer primary key autoincrement, " + LIST_HEADER
				+ " text not null, " + LIST + " text not null);";

		ListPopDBOpenHelper(Context c, String name, CursorFactory factory,
				int version) {
			super(c, name, factory, version);
		}

		// Called when no database exists on disk and the helper class needs
		// to create a new one.
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		// Called when there is a database version mismatch meaning that
		// the current version of the database on disk needs to be
		// upgraded to the newest version.
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Log the version upgrade.
			Log.w("ListDbAdapter", "Upgrading from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data");

			// Upgrade the existing database to conform to the new version.
		}

	}

}
