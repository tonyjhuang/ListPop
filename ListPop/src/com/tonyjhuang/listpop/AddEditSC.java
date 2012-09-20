package com.tonyjhuang.listpop;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

abstract class AddEditSC extends SherlockActivity {
	protected EditText listName, addItem;
	protected ListView mListView;
	protected ImageButton finish;
	protected AddArrayAdapter aa;
	protected long animationTime, beginAnimationTime;
	protected Context context;
	protected String TAG;

	// If user inputs Enter, add EditText text to ArrayList and
	// clear EditText.
	protected void hookUpAddItem() {
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

	// Add string from edittext to arrayadapter and refresh.
	protected void addItem(String t) {
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
						Animation anim = AnimationUtils.loadAnimation(context,
								R.anim.slidein);
						anim.setDuration(animDuration);
						refreshTime(animDuration + 300);
						Log.d(TAG, "Attempting to start animation on child 0.");
						mListView.getChildAt(0).startAnimation(anim);
					}
				},
				// Fixes bug where if there are no children in list,
				// the first item flashes briefly and then slides in.
						aa.getCount() == 0 ? 0 : mListView
								.getFirstVisiblePosition() * 40);
			}
		});

	}

	// Refresh beginAnimationTime and set new animationTime.
	protected void refreshTime(long newAnimationTime) {
		animationTime = newAnimationTime;
		beginAnimationTime = System.currentTimeMillis();
	}

	// Check if there are any running animations.
	private boolean notAnimating() {
		return System.currentTimeMillis() > (beginAnimationTime + animationTime);
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This is called when the Home (Up) button is pressed
			// in the Action Bar.
			Intent i = new Intent(this, StartActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);

			setResult(RESULT_CANCELED, i);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
