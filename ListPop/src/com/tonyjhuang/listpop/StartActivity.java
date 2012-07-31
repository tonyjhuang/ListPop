package com.tonyjhuang.listpop;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;
//import android.widget.SimpleCursorAdapter;

public class StartActivity extends Activity {
	private static final int ADD_ACTIVITY = 1;
	private static final int PRESETS_ACTIVITY = ADD_ACTIVITY + 1;
	private static final int EDIT_ACTIVITY = ADD_ACTIVITY + 2;
	private static final int DELETE_ID = Menu.FIRST;
	
	private DbAdapter2 mDbA2;
	private ListView mListView;
	private BaseAdapter adapter;
	private Cursor c;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);

		mDbA2 = new DbAdapter2(this);
		mDbA2.open();

		// Initialize cursor (result set of all rows from SQLite database).
		updateCursor();

		// Register listview for context menu and ItemClickListener.
		mListView = (ListView) findViewById(R.id.listselection);
		hookUpItemClickListener();
		registerForContextMenu(mListView);

		// Populate listview with a SimpleCursorAdapter.
		fillData();
	}

	private void hookUpItemClickListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> listview, View view,
					int position, long id) {
				// Fetch row requested from database.
				Cursor result = mDbA2.fetchListItem(id);

				// Query cursor for codified String.
				final int KEY_LIST_COLUMN_INDEX = result
						.getColumnIndex(DbAdapter2.LIST);

				// Regenerate object from String.
				List list = new List(result.getString(KEY_LIST_COLUMN_INDEX));

				// Pass List to PopActivity.
				Intent i = new Intent(StartActivity.this, PopActivity.class);
				i.putExtra(DbAdapter.LIST, list.toString());
				startActivity(i);

			}
		});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			// Delete row from database.
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			mDbA2.deleteListItem(info.id);

			// Repopulate the activity's listview.
			fillData();
			return true;
		}
		return super.onContextItemSelected(item);
	}

	// Initialize adapter with a SimpleCursorAdapter and populate listview.
	private void fillData() {
		updateCursor();
		adapter = new StartCursorAdapter(this, c);
		mListView.setAdapter(adapter);
	}

	private void toggleEdit() {
		if (mDbA2.isEmpty()) {
			Toast t = Toast.makeText(this, "Add some lists!",
					Toast.LENGTH_SHORT);
			t.show();
		} else {
			if (adapter instanceof EditViewAdapter)
				fillData();
			else {
				updateCursor();
				adapter = new EditViewAdapter(StartActivity.this, c);
				mListView.setAdapter(adapter);
			}
		}
	}

	// Update cursor result set.
	private void updateCursor() {
		c = mDbA2.fetchAll();
		c.moveToFirst();
	}

	// Called from EditViewAdapter. Deletes inputted row from database
	// and refreshes the cursor & adapter.
	public void deleteFromAdapter(long rowid) {
		mDbA2.deleteListItem(rowid);
		updateCursor();
		adapter = new EditViewAdapter(StartActivity.this, c);
		mListView.setAdapter(adapter);
	}

	public void startEditActivity(Long rowid) {
		Intent i = new Intent(this, EditActivity.class);
		i.putExtra(DbAdapter.ROWID, rowid);

		Cursor result = mDbA2.fetchListItem(rowid);
		final int KEY_LIST_COLUMN_INDEX = result.getColumnIndex(DbAdapter.LIST);
		List list = new List(result.getString(KEY_LIST_COLUMN_INDEX));

		i.putExtra(DbAdapter.LIST, list.toString());

		startActivityForResult(i, EDIT_ACTIVITY);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ADD_ACTIVITY:
			// Fall through to next case.
		case PRESETS_ACTIVITY:
			// Get data from intent, and enter it as a new row in the database.
			List list = new List(data.getStringExtra(DbAdapter2.LIST));
			Log.v("StartActivity", String.valueOf(list == null));
			List list2 = list;
			list2.toString();
			//mDbA2.enterList(list);

			// Then repopulate listview.
			fillData();
			break;

		case EDIT_ACTIVITY:
			// TODO
			break;

		}
	}

	// If adapter is a CustomCursorAdapter, mutate to SimpleCusorAdapter.
	// Otherwise, kill the activity.
	@Override
	public void onBackPressed() {
		if (adapter instanceof EditViewAdapter) {
			fillData();
		} else {
			finish();
		}

	}

	// Make sure you close the database to liberate system resources!
	@Override
	public void onDestroy() {
		super.onDestroy();
		mDbA2.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.action_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_edit:
			toggleEdit();
			break;

		case R.id.custom:
			Intent i = new Intent(StartActivity.this, AddActivity.class);
			startActivityForResult(i, ADD_ACTIVITY);
			break;

		case R.id.preset:
			Intent j = new Intent(StartActivity.this, PresetActivity.class);
			startActivityForResult(j, ADD_ACTIVITY);
			break;
		}

		return true;
	}

}
