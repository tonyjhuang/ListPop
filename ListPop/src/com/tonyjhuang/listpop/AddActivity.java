package com.tonyjhuang.listpop;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class AddActivity extends SherlockActivity implements AddArrayContainer {
	private EditText listName, addItem;
	private ListView mListView;
	private Button finish;
	private AddArrayAdapter aa;
	private long animationTime;
	private long beginAnimationTime;

	private static final String TAG = "AddActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);

		// Initialize UI elements.
		listName = (EditText) findViewById(R.id.listname);
		addItem = (EditText) findViewById(R.id.additem);
		finish = (Button) findViewById(R.id.finish);
		mListView = (ListView) findViewById(R.id.list);

		hookUpAddItem();
		hookUpFinish();

		// Initialize AddArrayAdapter with ArrayList.
		aa = new AddArrayAdapter(this, R.layout.list_item_d);
		mListView.setAdapter(aa);

		// Add up navigation affordance to the Action Bar.
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Add New List");

		refreshTime(0);
	}

	// If user inputs Enter, add EditText text to ArrayList and
	// clear EditText.
	private void hookUpAddItem() {
		addItem.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_ENTER:
						String currentText = addItem.getText().toString();
						addItem(currentText);
						return true;
					default:
						break;
					}
				}
				return false;
			}
		});
	}

	private void addItem(String t) {
		// Add item to array.
		aa.add(t);

		// Clear input field.
		addItem.setText("");

		// Animate first view.
		mListView.post(new Runnable() {
			public void run() {
				// Move ListView to top.
				mListView.smoothScrollToPosition(0);
				// Wait for the scrolling to stop...
				new Handler().postDelayed(new Runnable() {
					public void run() {
						long animDuration = 500;
						Animation anim = AnimationUtils.loadAnimation(
								AddActivity.this, R.anim.slidein);
						anim.setDuration(animDuration);
						refreshTime(animDuration + 300);
						Log.d(TAG, "Attempting to start animation on child 0.");
						mListView.getChildAt(0).startAnimation(anim);
					}
				}, (mListView.getFirstVisiblePosition() * 40));
			}
		});

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
					addItem.setError(getString(R.string.no_items));
				} else {
					Intent i = new Intent();
					String newListName = listName.getText().toString();
					i.putExtra(DbAdapter.LIST_HEADER, newListName);

					i.putStringArrayListExtra(DbAdapter.LIST, aa.getList());
					setResult(RESULT_OK, i);
					finish();
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
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
			startActivity(i);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void deleteFromAdapter(final int position) {
		Log.d(TAG, "deleteFromAdapter called");
		Log.d(TAG, "beginAnimationTime = " + beginAnimationTime);
		Log.d(TAG, "animating? " + !notAnimating());
		if (notAnimating()) {
			Log.d(TAG, "Ready to animate...");
			long animDuration = 500;
			Animation anim = AnimationUtils.loadAnimation(this,
					android.R.anim.slide_out_right);
			anim.setDuration(animDuration);
			refreshTime(animDuration);
			Log.d(TAG, "Time refreshed...");
			Log.d(TAG, "View to be animated's position = " + position);
			Log.d(TAG,
					"FirstVisiblePosition = "
							+ mListView.getFirstVisiblePosition());
			Log.d(TAG, "Attempting animate child at position "
					+ (position - mListView.getFirstVisiblePosition()) + "...");
			mListView
					.getChildAt(position - mListView.getFirstVisiblePosition())
					.startAnimation(anim);
			Log.d(TAG, "Row animating");

			// The deleting!
			new Handler().postDelayed(new Runnable() {

				public void run() {
					aa.remove(position);
				}

			}, anim.getDuration());

			Log.d(TAG, "Animation/deletion completed successfully!");
		}
	}

	// Refresh beginAnimationTime and set new animationTime.
	private void refreshTime(long newAnimationTime) {
		animationTime = newAnimationTime;
		beginAnimationTime = System.currentTimeMillis();
	}

	private boolean notAnimating() {
		return System.currentTimeMillis() > (beginAnimationTime + animationTime);
	}

}
