package com.tonyjhuang.listpop;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
//import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class StartActivity extends Activity {
	private static final int ADD_ACTIVITY = 1;
	private static final int PRESETS_ACTIVITY = ADD_ACTIVITY + 1;
	private static final int EDIT_ACTIVITY = ADD_ACTIVITY + 2;
	private static final int DELETE_ID = Menu.FIRST;
	private static final String TAG = "StartActivity";

	private DbAdapter mDbA;
	private ListView mListView;
	private BaseAdapter adapter;
	private Cursor c;
	private LayoutAnimationController controller;
	private long animationTime, beginAnimationTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		/*
		 * // Check if this is the first run of the activity through
		 * Preferences. //SharedPreferences settings =
		 * getPreferences(MODE_PRIVATE); //boolean firstRun =
		 * settings.getBoolean("firstRun", true); if (true) {
		 * 
		 * // Grab Splash handler. final ImageView splash = new ImageView(this);
		 * splash.setImageResource(R.drawable.splash);
		 * 
		 * // Define layout terms. RelativeLayout.LayoutParams lp = new
		 * RelativeLayout.LayoutParams(
		 * RelativeLayout.LayoutParams.WRAP_CONTENT,
		 * RelativeLayout.LayoutParams.WRAP_CONTENT);
		 * lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		 * lp.addRule(RelativeLayout.CENTER_VERTICAL);
		 * 
		 * // Grab RelativeLayout handler and add the splash screen. final
		 * RelativeLayout rl = (RelativeLayout) findViewById(R.id.relative);
		 * rl.addView(splash, lp);
		 * 
		 * // Call the fade out animation on the splash screen Animation
		 * myFadeInAnimation = AnimationUtils.loadAnimation(this,
		 * R.anim.fade_out); splash.startAnimation(myFadeInAnimation);
		 * 
		 * // Delete the splash screen after 3 seconds. new
		 * Handler().postDelayed(new Runnable() {
		 * 
		 * public void run() { rl.removeView(splash); }
		 * 
		 * }, 3000);
		 * 
		 * // Save the state //settings.edit().putBoolean("firstRun",
		 * false).commit(); }
		 */
		// Start SQLite database.
		mDbA = new DbAdapter(this);
		mDbA.open();

		// Register ListView for context menu and ItemClickListener.
		mListView = (ListView) findViewById(R.id.listselection);
		hookUpItemClickListener();
		registerForContextMenu(mListView);

		Intent i = getIntent();
		if (i.getBooleanExtra("Edit", false) == true)
			toggleEdit();
		else
			// Populate listview with a SimpleCursorAdapter.
			fillData();

		animateListView();

		int currentChildren = mListView.getCount();
		Log.d(TAG, "Current number of items in listview: " + currentChildren);
		
		if(currentChildren > 6)
			refreshTime(6 * 420 + 180);
		else
			refreshTime(currentChildren * 420 + 180);
		
		Log.d(TAG, "animationTime = " + animationTime);
		Log.d(TAG, "beginAnimationTime = " + beginAnimationTime);
	}

	private void animateListView() {
		controller = AnimationUtils.loadLayoutAnimation(this,
				R.anim.layout_controller);
		mListView.setLayoutAnimation(controller);
	}

	private void hookUpItemClickListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> list, View view,
					int position, long id) {
				if (notAnimating()) {
					// Fetch row requested from database.
					Cursor result = mDbA.fetchListItem(id);

					// Query cursor for codified String.
					final int KEY_LIST_COLUMN_INDEX = result
							.getColumnIndex(DbAdapter.LIST);
					String pList = result.getString(KEY_LIST_COLUMN_INDEX);

					// Query cursor for name of list.
					final int KEY_LIST_HEADER_COLUMN_INDEX = result
							.getColumnIndex(DbAdapter.LIST_HEADER);
					String pName = result
							.getString(KEY_LIST_HEADER_COLUMN_INDEX);

					// Start PopActivity with codified String.
					Intent i = new Intent(StartActivity.this, PopActivity.class);
					i.putExtra(DbAdapter.LIST_HEADER, pName);
					i.putExtra(DbAdapter.LIST, pList);
					startActivity(i);
				}

			}
		});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (notAnimating()) {
			super.onCreateContextMenu(menu, v, menuInfo);
			menu.add(0, DELETE_ID, 0, R.string.menu_delete);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			// Delete row from database.
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			mDbA.deleteListItem(info.id);

			// Repopulate the activity's listview.
			fillData();
			return true;
		}
		return super.onContextItemSelected(item);
	}

	// Initialize adapter with a SimpleCursorAdapter and populate listview.
	@SuppressWarnings("deprecation")
	private void fillData() {
		updateCursor();
		startManagingCursor(c);

		String[] from = new String[] { DbAdapter.LIST_HEADER };
		int[] to = new int[] { R.id.listitem };
		adapter = new SimpleCursorAdapter(StartActivity.this,
				R.layout.list_item, c, from, to);
		mListView.setAdapter(adapter);
	}

	// If adapter is a CustomCursorAdapter, mutate to SimpleCursorAdapter.
	// Otherwise, mutate to CustomCursorAdapter.
	private void toggleEdit() {
		if (mDbA.isEmpty()) {
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
		c = mDbA.fetchAll();
		c.moveToFirst();
	}

	/**** Callback ****/
	// Called from CustomCursorAdapter. Animates row,
	// then deletes from database.
	// Afterwards, refreshes the cursor & adapter.
	public void deleteFromAdapter(int pos, final long rowid) {
		if (notAnimating()) {
			// The animation!
			long animDuration = 500;
			Animation anim = AnimationUtils.loadAnimation(this,
					android.R.anim.slide_out_right);
			anim.setDuration(animDuration);
			refreshTime(animDuration);
			mListView.getChildAt(pos).startAnimation(anim);

			// The deleting!
			new Handler().postDelayed(new Runnable() {

				public void run() {
					mDbA.deleteListItem(rowid);
					updateCursor();
					adapter = new EditViewAdapter(StartActivity.this, c);
					mListView.setAdapter(adapter);
				}

			}, anim.getDuration());
		}
	}

	// Refresh beginAnimationTime and set new animationTime.
	private void refreshTime(long newAnimationTime) {
		animationTime = newAnimationTime;
		beginAnimationTime = System.currentTimeMillis();
		Log.d(TAG, "beginAnimationTime = " + beginAnimationTime);
	}

	private boolean notAnimating() {
		return System.currentTimeMillis() > (beginAnimationTime + animationTime);
	}

	// Start EditActivity with an Intent bundled with a row id,
	// list name, and codified string. This avoids exposing
	// EditActivity to the database and keeps all database transactions
	// local.
	public void startEditActivity(Long tag) {
		if (notAnimating()) {
			Intent i = new Intent(this, EditActivity.class);
			i.putExtra(DbAdapter.ROWID, tag);

			Cursor result = mDbA.fetchListItem(tag);
			final int KEY_LIST_COLUMN_INDEX = result
					.getColumnIndex(DbAdapter.LIST);
			String pList = result.getString(KEY_LIST_COLUMN_INDEX);

			final int KEY_LIST_HEADER_COLUMN_INDEX = result
					.getColumnIndex(DbAdapter.LIST_HEADER);
			String pName = result.getString(KEY_LIST_HEADER_COLUMN_INDEX);

			i.putExtra(DbAdapter.LIST, pList);
			i.putExtra(DbAdapter.LIST_HEADER, pName);

			startActivityForResult(i, EDIT_ACTIVITY);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_CANCELED) {

			switch (requestCode) {

			case ADD_ACTIVITY:

			case PRESETS_ACTIVITY:
				// Get data from intent, and enter it as a new row in the
				// database.
				String newListHeader = data
						.getStringExtra(DbAdapter.LIST_HEADER);
				ArrayList<String> newList = data
						.getStringArrayListExtra(DbAdapter.LIST);
				mDbA.enterList(newListHeader, newList);

				// Then repopulate listview.
				fillData();

				mListView.post(new Runnable() {
					public void run() {
						long animDuration = 500;
						Animation anim = AnimationUtils.loadAnimation(
								StartActivity.this, R.anim.slidein);
						anim.setDuration(animDuration);
						refreshTime(animDuration);
						mListView.getChildAt(
								mListView.getFirstVisiblePosition())
								.startAnimation(anim);
					}
				});

				break;

			case EDIT_ACTIVITY:
				long rowid = data.getLongExtra(DbAdapter.ROWID, 0);
				if (data.getStringExtra(DbAdapter.LIST_HEADER) == null) {

					mDbA.deleteListItem(rowid);
					updateCursor();
					adapter = new EditViewAdapter(StartActivity.this, c);
					mListView.setAdapter(adapter);
					break;
				} else {
					String title = data.getStringExtra(DbAdapter.LIST_HEADER);
					ArrayList<String> list = data
							.getStringArrayListExtra(DbAdapter.LIST);

					mDbA.updateListItem(rowid, title, list);
					adapter.notifyDataSetChanged();
					break;
				}
			}

		}

		// mListView.setLayoutAnimation(controller);
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
		mDbA.close();
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
			if (notAnimating()) {
				toggleEdit();
				Log.d(TAG, "Current time = " + System.currentTimeMillis());
			}
			break;

		case R.id.custom:
			Intent i = new Intent(StartActivity.this, AddActivity.class);
			startActivityForResult(i, ADD_ACTIVITY);
			refreshTime(0);
			break;

		case R.id.preset:
			Intent j = new Intent(StartActivity.this, PresetActivity.class);
			startActivityForResult(j, PRESETS_ACTIVITY);
			refreshTime(0);
			break;
		}

		return true;
	}

}
