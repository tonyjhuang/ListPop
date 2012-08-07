package com.tonyjhuang.listpop;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ListView;

public class EightBallFragment extends Fragment {
	private static final String TAG = "EightBallFragment";
	
	ListView mListView;
	EditText add;
	AddArrayAdapter aa;
	private long animationTime, beginAnimationTime;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.eight_ball, container, false);
		refreshTime(0);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// Initialize UI references.
		mListView = (ListView) view.findViewById(R.id.eightballlist);
		add = (EditText) view.findViewById(R.id.additem);

		add.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_ENTER:
						String currentText = add.getText().toString();
						aa.add(currentText);
						add.setText("");
						return true;
					default:
						break;
					}
				}
				return false;
			}
		});

		ArrayList<String> eightBall = new ArrayList<String>();
		Collections.addAll(eightBall,
				getResources().getStringArray(R.array.eight_ball));

		aa = new AddArrayAdapter(getActivity(), R.layout.list_item_d, eightBall);
		mListView.setAdapter(aa);
	}

	public ArrayList<String> getList() {
		return aa.getList();
	}

	public void deleteFromActivity(final int position) {
		if (notAnimating()) {
			Log.d(TAG, "Ready to animate...");
			long animDuration = 500;
			Animation anim = AnimationUtils.loadAnimation(getActivity(),
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
