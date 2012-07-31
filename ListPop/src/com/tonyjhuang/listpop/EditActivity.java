package com.tonyjhuang.listpop;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class EditActivity extends Activity {
	private ListView mListView;
	private EditText listName;
	private Button finish;
	private AddArrayAdapter aa;
	private long rowid;
	private View buttonHeader;
	private EditText editHeader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);

		// Grab UI handles.
		mListView = (ListView) findViewById(R.id.list);
		listName = (EditText) findViewById(R.id.listname);
		finish = (Button) findViewById(R.id.finish);

		String listname = getIntent().getStringExtra(DbAdapter.LIST_HEADER);
		listName.setText(listname);

		String codifiedList = getIntent().getStringExtra(DbAdapter.LIST);
		ArrayList<String> list = interpret(codifiedList);
		
		rowid = getIntent().getLongExtra(DbAdapter.ROWID, 0);

		hookUpFinish();

		// Populate listview with header + list items.
		LayoutInflater inflater = getLayoutInflater();
		buttonHeader = inflater.inflate(R.layout.edit_buttonheader,
				mListView, false);
		mListView.addHeaderView(buttonHeader);

		aa = new AddArrayAdapter(this, R.layout.list_item_d, list);
		mListView.setAdapter(aa);
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
					//TODO: list size = 0?
				} else {
					Intent i = new Intent();
					i.putExtra(DbAdapter.LIST_HEADER, listName.getText().toString());
					Log.v("EditActivity", "list: " + aa.getList().toString());
					i.putStringArrayListExtra(DbAdapter.LIST, aa.getList());
					i.putExtra(DbAdapter.ROWID, rowid);
					setResult(RESULT_OK, i);
					finish();
				}
			}
		});
	}

}
