package com.tonyjhuang.listpop;

import java.util.ArrayList;
import java.util.Random;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
//import android.widget.ArrayAdapter;
import android.widget.Button;
//import android.widget.ListView;
import android.widget.TextView;

public class PopActivity extends Activity {
	private Long ROWID;
	private DbAdapter mDbA;
	private TextView listHeader, popResult;
	private Button pop;
	private ArrayList<String> list = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pop);
		
		//Open SQLite database.
		mDbA = new DbAdapter(this);
		mDbA.open();
		
		//Get row id from passed in intent.
		ROWID = getIntent().getLongExtra(DbAdapter.ROWID, 0);

		Cursor mCursor = mDbA.fetchListItem(ROWID);
		
		//Set list header text.
		int KEY_LIST_HEADER_COLUMN_INDEX = mCursor
				.getColumnIndex(DbAdapter.LIST_HEADER);
		String listname = mCursor.getString(KEY_LIST_HEADER_COLUMN_INDEX);
		listHeader = (TextView) findViewById(R.id.listname);
		listHeader.setText(listname);
		
		//Retrieve the codified String from the Cursor result set.
		int KEY_LIST_COLUMN_INDEX = mCursor
				.getColumnIndex(DbAdapter.LIST);
		String sDisplay = mCursor.getString(KEY_LIST_COLUMN_INDEX);
		
		//Initialize ArrayList variable.
		list = interpret(sDisplay);
		
		popResult = (TextView) findViewById(R.id.popresult);
		
		//Initialize and add OnClickListener to pop button.
		pop = (Button) findViewById(R.id.pop);
		hookUpPop();

		//Add up navigation affordance to the Action Bar.
		ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Add List");
		
	}

	//Create a random number generator, and set the popResult textview
	// text to a random String returned from the list field.
	private void hookUpPop() {
		pop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Random generator = new Random();

				int totalNumberOfItems = list.size();
				
				int randomIndex = generator.nextInt(totalNumberOfItems);
				String randomString = list.get(randomIndex);
				popResult.setText(randomString);
			}
		});
	}

	//Returns an ArrayList of Strings given a codified String
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

	//Implementation of Ancestral Navigation via the Action Bar.
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // This is called when the Home (Up) button is pressed
	            // in the Action Bar. Code was taken from:
	        	// http://developer.android.com/training/implementing-navigation/ancestral.html
	            Intent upIntent = new Intent(this, StartActivity.class);
	            upIntent.addFlags(
	                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
	                    Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(upIntent);
	            finish();
	            return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	//Close up that database, son!
	@Override
	public void onDestroy() {
		super.onDestroy();
		mDbA.close();
	}
}
