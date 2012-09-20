package com.tonyjhuang.listpop;

import java.util.ArrayList;

import com.actionbarsherlock.app.ActionBar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ListView;

public class EditActivity extends AddEditSC implements AddArrayContainer {
	private long rowid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);

		// Initialize superclass handlers.
		TAG = "EditActivity";
		context = this;

		// Grab UI handles.
		mListView = (ListView) findViewById(R.id.list);
		listName = (EditText) findViewById(R.id.listname);
		finish = (ImageButton) findViewById(R.id.finish);
		addItem = (EditText) findViewById(R.id.additem);

		// Retrieve information about list from intent.
		Intent i = getIntent();
		listName.setText(i.getStringExtra(DbAdapter.LIST_HEADER));
		ArrayList<String> list = interpret(i.getStringExtra(DbAdapter.LIST));
		rowid = i.getLongExtra(DbAdapter.ROWID, 0);

		hookUpAddItem();
		hookUpFinish();

		aa = new AddArrayAdapter(this, R.layout.list_item_d, list);
		mListView.setAdapter(aa);

		// Add up navigation affordance to the Action Bar.
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Edit List");

		refreshTime(0);
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
				} else if (aa.getCount() == 0) {
					createDeleteDialog();
				} else {
					Intent i = new Intent();
					i.putExtra(DbAdapter.LIST_HEADER, listName.getText()
							.toString());
					i.putStringArrayListExtra(DbAdapter.LIST, aa.getList());
					i.putExtra(DbAdapter.ROWID, rowid);
					setResult(RESULT_OK, i);
					finish();
				}
			}
		});
	}

	private void createDeleteDialog() {
		// Create dialog that asks the user if they want to delete the list.
		AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
		builder.setMessage("Delete list?");
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				finishDelete();
			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void finishDelete() {
		Intent i = new Intent();
		i.putExtra(DbAdapter.ROWID, rowid);
		setResult(RESULT_OK, i);
		finish();
	}

	
}
