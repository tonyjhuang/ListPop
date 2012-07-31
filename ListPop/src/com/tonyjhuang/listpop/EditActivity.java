package com.tonyjhuang.listpop;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class EditActivity extends Activity {
	ListView mListView;
	EditText listName;
	ArrayList<String> list;
	AddArrayAdapter aaa;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);

		// Grab UI handles.
		mListView = (ListView) findViewById(R.id.list);
		listName = (EditText) findViewById(R.id.listname);

		String listname = getIntent().getStringExtra(DbAdapter.LIST_HEADER);
		listName.setText(listname);
		
		String codifiedList = getIntent().getStringExtra(DbAdapter.LIST);
		list = interpret(codifiedList);

		LayoutInflater inflater = getLayoutInflater();
		View listHeading = inflater.inflate(R.layout.edit_list_header,
				mListView, false);
		mListView.addHeaderView(listHeading);
		
		aaa = new AddArrayAdapter(this, R.layout.list_item_d, list);
		mListView.setAdapter(aaa);
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
}
