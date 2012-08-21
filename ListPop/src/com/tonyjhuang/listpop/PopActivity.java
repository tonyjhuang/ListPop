package com.tonyjhuang.listpop;

import java.util.ArrayList;
import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class PopActivity extends SherlockActivity {
	private static final String TAG = "PopActivity";

	private ImageButton pop;
	private ArrayList<String> list;
	private int totalNumberOfItems;
	private Random generator = new Random();
	private TextView resultDisplay;
	private ImageView small, large;
	private Animator a;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pop);

		// Before anything else, make bubbles invisible!
		large = (ImageView) findViewById(R.id.bubblelarge);
		large.setAlpha(0);
		small = (ImageView) findViewById(R.id.bubblesmall);
		small.setAlpha(0);

		// Initialize passed in intent and retrieve extras.
		Bundle extras = getIntent().getExtras();
		String listName = extras.getString(DbAdapter.LIST_HEADER);
		Log.d(TAG, "List name = " + listName);
		String codifiedList = extras.getString(DbAdapter.LIST);
		Log.d(TAG, "Codified list = " + codifiedList);

		Log.d(TAG, "Intent extras successfully retrieved.");

		// Initialize ArrayList variable.
		list = DbAdapter.interpret(codifiedList);
		totalNumberOfItems = list.size();

		resultDisplay = (TextView) findViewById(R.id.popresult);

		// Initialize and add OnClickListener to the transient pop button.
		pop = (ImageButton) findViewById(R.id.pop);
		Log.d(TAG, "Button handle initialized.");
		hookUpPop();
		Log.d(TAG, "Pop button hooked up with onClickListener");

		// Add up navigation affordance to the Action Bar.
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(listName);
		
		a = null;

	}

	// Create a random number generator, and set the popResult textview
	// text to a random String returned from the list field.
	// Removes this button from layout and adds pPop.
	private void hookUpPop() {
		pop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				int randomIndex = generator.nextInt(totalNumberOfItems);
				Log.d(TAG, "randomIndex initialized. randomIndex = "
						+ randomIndex);

				String result = list.get(randomIndex);
				Log.d(TAG, "Result = " + result);
				
				// Start animation.
				a = new Animator(PopActivity.this, result);
				a.run();
				Log.d(TAG, "Animator object created");
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This is called when the Home (Up) button is pressed
			// in the Action Bar. Code was taken from:
			// http://developer.android.com/training/implementing-navigation/ancestral.html
			Intent upIntent = new Intent(this, StartActivity.class);
			upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(upIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	class Animator {
		String result;

		// private static final String TAG = "PopActivity.Animator";

		public Animator(SherlockActivity _c, String r) {
			result = r;
			// Reset screen.
			resultDisplay.setText("");
			small.setAlpha(0);
			large.setAlpha(0);
		}

		public void run() {
			Handler h = new Handler();

			h.postDelayed(new Runnable() {
				@Override
				public void run() {
					small.setAlpha(255);
				}
			}, 300);

			h.postDelayed(new Runnable() {
				@Override
				public void run() {
					large.setAlpha(255);
				}
			}, 800);
			
			h.postDelayed(new Runnable() {
				@Override
				public void run() {
					resultDisplay.setText(result);
				}
			}, 1300);
		}
	}
}
