package com.tonyjhuang.listpop;

import com.actionbarsherlock.app.ActionBar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class AddActivity extends AddEditSC implements AddArrayContainer {
	private static final int PRESET_ACTIVITY_CODE = 0;
	Button preset;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);

		// Initialize superclass handlers
		TAG = "AddActivity";
		context = this;

		// Initialize UI elements.
		listName = (EditText) findViewById(R.id.listname);
		addItem = (EditText) findViewById(R.id.additem);
		finish = (Button) findViewById(R.id.finish);
		mListView = (ListView) findViewById(R.id.list);

		hookUpAddItem();
		hookUpFinish();

		// Initialize AddArrayAdapter with ArrayList.
		aa = new AddArrayAdapter(this, R.layout.list_item_d);
		mListView.setAdapter(aa);

		// Add up navigation affordance to the Action Bar.
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Add New List");

		refreshTime(0);
		Log.d(TAG, "Attempting retrieval of preset handler.");
		preset = (Button) findViewById(R.id.preset);
		Log.d(TAG, "Success!");
		Log.d(TAG, "Attempting connection of OnClickListener...");
		hookUpPreset();
	}

	private void hookUpPreset(){
		Log.d(TAG, "Starting now!");
		preset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(AddActivity.this, PresetActivity.class);
				Log.d(TAG, "Starting PresetActivity...");
				startActivityForResult(i, PRESET_ACTIVITY_CODE);
			}
		});
		Log.d(TAG, "Success!!");
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
					addItem.setError(getString(R.string.no_items));
				} else {
					Intent i = new Intent();
					String newListName = listName.getText().toString();
					i.putExtra(DbAdapter.LIST_HEADER, newListName);

					i.putStringArrayListExtra(DbAdapter.LIST, aa.getList());
					setResult(RESULT_OK, i);
					finish();
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
	}

}
