package com.tonyjhuang.listpop;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("unused")
public class EditViewAdapter extends CursorAdapter {
	private LayoutInflater mInflater;
	private Cursor cursor;
	private Context context;
	private int ROWID_INDEX, LIST_HEADER_INDEX;
	private ImageButton edit, delete;

	@SuppressWarnings("deprecation")
	public EditViewAdapter(Context _context, Cursor _c) {
		//Cache the LayoutInflate to avoid asking for a new one each time.
		super(_context, _c);

		mInflater = LayoutInflater.from(_context);
		this.cursor = _c;
		this.context = _context;

		ROWID_INDEX = cursor.getColumnIndex(DbAdapter.ROWID);
		LIST_HEADER_INDEX = cursor.getColumnIndex(DbAdapter.LIST_HEADER);
	}

	@Override
	public void bindView(View view, final Context context, Cursor cursor) {
		//Initialize UI references.
		TextView listHeader = (TextView) view.findViewById(R.id.listname);
		edit = (ImageButton) view.findViewById(R.id.editline);
		delete = (ImageButton) view.findViewById(R.id.deleteline);

		//Set list header textbox.
		listHeader.setText(cursor.getString(LIST_HEADER_INDEX));

		//Add rowid tag to buttons for easier OnClickListener implementations.
		long id = cursor.getLong(ROWID_INDEX);
		edit.setTag(id);
		delete.setTag(id);

		// Produce a toast when button is clicked.
		edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast t = Toast.makeText(context, v.getTag().toString(),
						Toast.LENGTH_SHORT);

				t.show();
			}
		});
		
		//Call delete function in parent activity with row id obtained 
		// by getTag().
		delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				long rowid = (Long) v.getTag();
				((StartActivity) context).deleteFromAdapter(rowid);
			}
		});
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.list_item_e, null);
	}

}
