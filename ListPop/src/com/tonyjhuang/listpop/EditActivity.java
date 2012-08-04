package com.tonyjhuang.listpop;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class EditActivity extends Activity implements AddArrayContainer {
	static final int DIALOG_DELETE_ID = 0;
	private static final String TAG = "EditActivity";

	private ListView mListView;
	private EditText listName, additem;
	private Button finish;
	private AddArrayAdapter aa;
	private long rowid;
	private long animationTime;
	private long beginAnimationTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);

		// Grab UI handles.
		mListView = (ListView) findViewById(R.id.list);
		listName = (EditText) findViewById(R.id.listname);
		finish = (Button) findViewById(R.id.finish);
		additem = (EditText) findViewById(R.id.additem);

		// Retrieve information about list from intent.
		Intent i = getIntent();
		listName.setText(i.getStringExtra(DbAdapter.LIST_HEADER));
		ArrayList<String> list = interpret(i.getStringExtra(DbAdapter.LIST));
		rowid = i.getLongExtra(DbAdapter.ROWID, 0);

		hookUpFinish();

		hookUpAddItem();

		aa = new AddArrayAdapter(this, R.layout.list_item_d, list);
		mListView.setAdapter(aa);

		// Add up navigation affordance to the Action Bar.
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Edit List");

		refreshTime(0);
	}

	// Returns an ArrayList of Strings given a codified String
	// returned from the SQLite database.
	private ArrayList<String> interpret(String s) {
		ArrayList<String> a = new ArrayList<String>();
		String current = s;

		int nextCommaIndex = current.indexOf("|");

		while (current.length() != 0) {
			a.add(current.substring(0, nextCommaIndex));
			current = current.substring(nextCommaIndex + 1);
			nextCommaIndex = current.indexOf("|");
		}

		return a;
	}

	// Error if there is no header or if there are no items.
	// Otherwise, put list name and ArrayList into intent and finish
	// the activity.
	private void hookUpFinish() {
		finish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listName.getText().toString().equals("")) {
					listName.setError(getString(R.string.no_header));
				} else if (aa.getCount() == 0) {
					createDeleteDialog();
				} else {
					Intent i = new Intent();
					i.putExtra(DbAdapter.LIST_HEADER, listName.getText()
							.toString());
					i.putStringArrayListExtra(DbAdapter.LIST, aa.getList());
					i.putExtra(DbAdapter.ROWID, rowid);
					setResult(RESULT_OK, i);
					finish();
				}
			}
		});
	}

	private void createDeleteDialog() {
		// Create dialog that asks the user if they want to delete the list.
		AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
		builder.setMessage("Delete list?");
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				finishDelete();
			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	// Sets up new item EditText with a shiny new OnKeyListener for ENTER.
	private void hookUpAddItem() {
		additem.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_ENTER:
						String currentText = additem.getText().toString();
						aa.add(currentText);
						additem.setText("");
						return true;
					default:
						break;
					}
				}
				return false;
			}
		});
	}

	private void finishDelete() {
		Intent i = new Intent();
		i.putExtra(DbAdapter.ROWID, rowid);
		setResult(RESULT_OK, i);
		finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This is called when the Home (Up) button is pressed
			// in the Action Bar.
			Intent i = new Intent(this, StartActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			i.putExtra("Edit", true);
			startActivity(i);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void deleteFromAdapter(final int position) {
		Log.d(TAG, "deleteFromAdapter called");
		if (notAnimating()) {
			Log.d(TAG, "Ready to animate...");
			long animDuration = 500;
			Animation anim = AnimationUtils.loadAnimation(this,
					android.R.anim.slide_out_right);
			anim.setDuration(animDuration);
			refreshTime(animDuration);
			Log.d(TAG, "Time refreshed...");
			mListView.getChildAt(position).startAnimation(anim);
			Log.d(TAG, "Row animating");

			// The deleting!
			new Handler().postDelayed(new Runnable() {

				public void run() {
					aa.remove(position);
				}

			}, anim.getDuration());
		}
	}
	
	// Refresh beginAnimationTime and set new animationTime.
	private void refreshTime(long newAnimationTime) {
		animationTime = newAnimationTime;
		beginAnimationTime = System.currentTimeMillis();
	}

	private boolean notAnimating() {
		Log.d(TAG, "Current Time = " + System.currentTimeMillis());
		Log.d(TAG, "beginAnimationTime + " + beginAnimationTime);
		Log.d(TAG, "animationTime + " + animationTime);
		return System.currentTimeMillis() > (beginAnimationTime + animationTime);
	}
}
