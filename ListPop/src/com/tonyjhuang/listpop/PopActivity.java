package com.tonyjhuang.listpop;

import java.util.ArrayList;
import java.util.Random;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class PopActivity extends Activity {
	private TextView listHeader, popResult;
	private Button tPop, pPop;
	private ArrayList<String> list = new ArrayList<String>();
	private int totalNumberOfItems;
	private RelativeLayout layout;
	private Random generator = new Random();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pop);

		// Grab Layout handle.
		layout = (RelativeLayout) findViewById(R.id.rlayout);

		// Initialize passed in intent and retrieve extras.
		Bundle extras = getIntent().getExtras();
		String listName = extras.getString(DbAdapter.LIST_HEADER);
		String codifiedList = extras.getString(DbAdapter.LIST);

		// Set list header text.
		listHeader = (TextView) findViewById(R.id.listname);
		listHeader.setText(listName);

		// Initialize ArrayList variable.
		list = interpret(codifiedList);
		totalNumberOfItems = list.size();

		popResult = (TextView) findViewById(R.id.popresult);

		// Initialize and add OnClickListener to the transient pop button.
		tPop = (Button) findViewById(R.id.pop);
		hookUpTransientPop();

		// Initialize permenant pop button.
		pPop = new Button(this);

		// Initialize LayoutParams for permenant pop button.
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		pPop.setLayoutParams(params);
		pPop.setText(getResources().getString(R.string.poplist));

		// Add up navigation affordance to the Action Bar.
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Add List");

	}

	// Create a random number generator, and set the popResult textview
	// text to a random String returned from the list field.
	// Removes this button from layout and adds pPop.
	private void hookUpTransientPop() {
		tPop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				int randomIndex = generator.nextInt(totalNumberOfItems);
				popResult.setText(list.get(randomIndex));

				// Remove View from Layout.
				layout.removeView(tPop);
				layout.addView(pPop);
				hookUpPermanentPop();
			}
		});
	}

	private void hookUpPermanentPop() {
		pPop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				int randomIndex = generator.nextInt(totalNumberOfItems);
				popResult.setText(list.get(randomIndex));
			}
		});
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
}
