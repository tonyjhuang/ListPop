package com.tonyjhuang.listpop;

import java.util.ArrayList;
import java.util.Random;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PopActivity extends SherlockActivity {
	private static final String TAG = "PopActivity";

	private Button pop;
	private ArrayList<String> list;
	private int totalNumberOfItems;
	private Random generator = new Random();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pop);

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

		// Initialize and add OnClickListener to the transient pop button.
		pop = (Button) findViewById(R.id.pop);
		Log.d(TAG, "Button handle initialized.");
		hookUpPop();
		Log.d(TAG, "Pop button hooked up with onClickListener");

		// Add up navigation affordance to the Action Bar.
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(listName);

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

	static class Animator {
		RelativeLayout layout;
		String result;
		TextView resultDisplay;

		public Animator(SherlockActivity _c, String r) {
			result = r;
			LayoutInflater factory = 
				((SherlockActivity) _c).getLayoutInflater();
			
			// Inflate pop layout and grab TextView and layout handler.
			final View tempLayout = factory.inflate(R.layout.pop, null);
			resultDisplay = 
				(TextView) tempLayout.findViewById(R.id.popresult);
			layout = 
				(RelativeLayout) tempLayout.findViewById(R.id.rlayout);
			
			// We want the text to be invisible until the animation finishes.
			resultDisplay.setText("");
		}
	}
}
