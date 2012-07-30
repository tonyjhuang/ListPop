package com.tonyjhuang.listpop;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class AddActivity extends Activity {
	EditText listName, listEntry;
	ListView currentEntries;
	Button finish;
	AddArrayAdapter aa;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);

		// Initialize UI elements.
		listName = (EditText) findViewById(R.id.listnamecreation);
		listEntry = (EditText) findViewById(R.id.listitemcreation);
		finish = (Button) findViewById(R.id.finishbutton);
		currentEntries = (ListView) findViewById(R.id.newitems);

		hookUpListEntry();
		hookUpFinish();

		// Initialize AddArrayAdapter with ArrayList.
		aa = new AddArrayAdapter(this);
		currentEntries.setAdapter(aa);

		// Add up navigation affordance to the Action Bar.
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Add List");
	}

	// If user inputs Enter, add EditText text to ArrayList and
	// clear EditText.
	private void hookUpListEntry() {
		listEntry.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_ENTER:
						String currentText = listEntry.getText().toString();
						aa.add(currentText);
						listEntry.setText("");
						return true;
					default:
						break;
					}
				}
				return false;
			}
		});
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
					listEntry.setError(getString(R.string.no_items));
				} else {
					Intent i = new Intent();
					i.putExtra("list_header", listName.getText().toString());
					i.putStringArrayListExtra("list", aa.getList());
					setResult(1, i);
					finish();
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		setResult(0);
		finish();
	}

}
