package com.tonyjhuang.listpop;

import java.util.Hashtable;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class EditViewAdapter extends CursorAdapter {
	private static final String ROWID = "rowid";
	private static final String POSITION = "position";
	private static final String TAG = "EditViewAdapter";

	private double density;
	private LayoutInflater mInflater;
	private Cursor cursor;
	private Context context;
	private int ROWID_INDEX, LIST_HEADER_INDEX;
	private ImageButton edit, delete;

	@SuppressWarnings("deprecation")
	public EditViewAdapter(Context _context, Cursor _c) {
		// Cache the LayoutInflate to avoid asking for a new one each time.
		super(_context, _c);

		mInflater = LayoutInflater.from(_context);
		this.cursor = _c;
		this.context = _context;

		ROWID_INDEX = cursor.getColumnIndex(DbAdapter.ROWID);
		LIST_HEADER_INDEX = cursor.getColumnIndex(DbAdapter.LIST_HEADER);

		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);

		switch (metrics.densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
			density = .75;
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			density = 1.0;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			density = 1.5;
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			density = 2.0;
			break;
		}

		Log.d(TAG, "Screen density multiplier: " + density);
	}

	@Override
	public void bindView(View view, final Context context, Cursor cursor) {
		// Initialize UI references.
		TextView listHeader = (TextView) view.findViewById(R.id.listname);
		edit = (ImageButton) view.findViewById(R.id.editline);
		delete = (ImageButton) view.findViewById(R.id.deleteline);

		// Set list header textbox.
		listHeader.setText(cursor.getString(LIST_HEADER_INDEX));
		listHeader.setSelected(true);

		// Add rowid tag to buttons for easier OnClickListener implementations.
		Hashtable<String, Integer> tag = new Hashtable<String, Integer>();
		int rowid = (int) cursor.getLong(ROWID_INDEX);
		int pos = cursor.getPosition();
		tag.put(ROWID, rowid);
		tag.put(POSITION, pos);
		edit.setTag(tag);
		delete.setTag(tag);

		// Starts EditActivity on the selected list.
		edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				@SuppressWarnings("unchecked")
				Hashtable<String, Integer> table = (Hashtable<String, Integer>) v
						.getTag();
				((StartActivity) context).startEditActivity((long) table
						.get(ROWID));

			}
		});

		// Call delete function in parent activity with row id obtained
		// by getTag().
		delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				@SuppressWarnings("unchecked")
				Hashtable<String, Integer> table = (Hashtable<String, Integer>) v
						.getTag();
				int pos = table.get(POSITION);
				((StartActivity) context).deleteFromAdapter(pos,
						(long) table.get(ROWID));
				v.setTag(table);

			}
		});
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		Log.d(TAG, "Current String = " + cursor.getString(LIST_HEADER_INDEX));
		Log.d(TAG, "Current String Length = " + cursor.getString(LIST_HEADER_INDEX).length());
		
		String current = cursor.getString(LIST_HEADER_INDEX);
		if(current.length() >= (16 * density))
			return mInflater.inflate(R.layout.list_item_e_long, null);
		else
			return mInflater.inflate(R.layout.list_item_e, null);
	}

}
