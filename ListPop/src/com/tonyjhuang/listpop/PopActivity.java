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
	;
	// private ListView mListView;
	private Long mRowId;
	private DbAdapter mDbA;
	private TextView listHeader, popResult;
	private Button pop;
	// ArrayAdapter<String> aa;
	private ArrayList<String> list = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pop);
		
		mDbA = new DbAdapter(this);
		mDbA.open();
		
		Bundle extras = getIntent().getExtras();
		mRowId = extras.getLong(DbAdapter.ROWID);

		Cursor mCursor = mDbA.fetchListItem(mRowId);
		
		int KEY_LIST_HEADER_COLUMN_INDEX = mCursor
				.getColumnIndex(DbAdapter.LIST_HEADER);
		String listname = mCursor.getString(KEY_LIST_HEADER_COLUMN_INDEX);
		listHeader = (TextView) findViewById(R.id.listname);
		listHeader.setText(listname);

		// mListView = (ListView) findViewById(R.id.listview);
		
		int KEY_LIST_COLUMN_INDEX = mCursor
				.getColumnIndex(DbAdapter.LIST);
		String sDisplay = mCursor.getString(KEY_LIST_COLUMN_INDEX);
		list = interpret(sDisplay);
		
		popResult = (TextView) findViewById(R.id.popresult);
		pop = (Button) findViewById(R.id.pop);
		hookUpPop();
		// fillData();

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

	/*
	 * @SuppressWarnings("deprecation") private void fillData(){ Cursor mCursor
	 * = mDbA.fetchListItem(mRowId); startManagingCursor(mCursor);
	 * 
	 * int KEY_LIST_COLUMN_INDEX =
	 * mCursor.getColumnIndexOrThrow(DbAdapter.LIST); String sDisplay =
	 * mCursor.getString(KEY_LIST_COLUMN_INDEX); ArrayList<String> aDisplay =
	 * interpret(sDisplay);
	 * 
	 * aa = new ArrayAdapter<String>(this, R.layout.list_item, aDisplay);
	 * mListView.setAdapter(aa); }
	 */

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
