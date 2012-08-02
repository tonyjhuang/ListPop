package com.tonyjhuang.listpop;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class EditActivity extends Activity {
	static final int DIALOG_DELETE_ID = 0;

	private ListView mListView;
	private EditText listName, additem;
	private Button finish, newitem;
	private AddArrayAdapter aa;
	private long rowid;
	private View buttonHeader;
	private View editHeader;

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

		// Initialize both headers that will be added dynamically.
		LayoutInflater inflater = getLayoutInflater();
		buttonHeader = inflater.inflate(R.layout.edit_buttonheader, mListView,
				false);
		editHeader = inflater.inflate(R.layout.edit_editheader, mListView,
				false);

		// Add buttonHeader to the listview
		mListView.addHeaderView(buttonHeader);

		newitem = (Button) findViewById(R.id.newitem);
		hookUpNewItem();

		aa = new AddArrayAdapter(this, R.layout.list_item_d, list);
		mListView.setAdapter(aa);

		// Add up navigation affordance to the Action Bar.
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Edit List");
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

	private void hookUpNewItem() {
		newitem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListView.setAdapter(null);
				mListView.addHeaderView(editHeader);
				mListView.removeHeaderView(buttonHeader);
				mListView.setAdapter(aa);
				hookUpAddItem();
			}
		});
	}

	private void hookUpAddItem() {
		additem = (EditText) findViewById(R.id.edititem);
		additem.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_ENTER:
						String currentText = additem.getText().toString();
						aa.add(currentText);
						additem.setText("");

						mListView.setAdapter(null);
						mListView.removeHeaderView(editHeader);
						mListView.addHeaderView(buttonHeader);
						mListView.setAdapter(aa);
						// aa.notifyDataSetChanged();
						return true;
					default:
						break;
					}
				}
				return false;
			}
		});
	}

	private void finishDelete() {
		Intent i = new Intent();
		i.putExtra(DbAdapter.ROWID, rowid);
		setResult(RESULT_OK, i);
		finish();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // This is called when the Home (Up) button is pressed
	            // in the Action Bar.
	            Intent i = new Intent(this, StartActivity.class);
	            i.addFlags(
	                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
	                    Intent.FLAG_ACTIVITY_NEW_TASK);
	            i.putExtra("Edit", true);
	            startActivity(i);
	            finish();
	            return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

}
