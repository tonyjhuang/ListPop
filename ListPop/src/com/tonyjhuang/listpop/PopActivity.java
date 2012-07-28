package com.tonyjhuang.listpop;

import java.util.ArrayList;
import java.util.Random;

import android.app.ActionBar;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
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
		
		//Initialize ArrayList variable.
		int KEY_LIST_COLUMN_INDEX = mCursor
				.getColumnIndex(DbAdapter.LIST);
		String sDisplay = mCursor.getString(KEY_LIST_COLUMN_INDEX);
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
	public void onDestroy() {
		super.onDestroy();
		mDbA.close();
	}
}
