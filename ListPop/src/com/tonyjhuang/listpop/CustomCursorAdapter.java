package com.tonyjhuang.listpop;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("unused")
public class CustomCursorAdapter extends CursorAdapter {
	private LayoutInflater mInflater;
	private Cursor cursor;
	private Context context;
	private int ROWID_INDEX, LIST_HEADER_INDEX;
	private Button editLine, deleteLine;

	@SuppressWarnings("deprecation")
	public CustomCursorAdapter(Context _context, Cursor _c) {
		// Cache the LayoutInflate to avoid asking for a new one each time.
		super(_context, _c);

		mInflater = LayoutInflater.from(_context);
		this.cursor = _c;
		this.context = _context;

		ROWID_INDEX = cursor.getColumnIndex(DbAdapter.ROWID);
		LIST_HEADER_INDEX = cursor.getColumnIndex(DbAdapter.LIST_HEADER);
	}

	@Override
	public void bindView(View view, final Context context, Cursor cursor) {
		TextView listHeader = (TextView)view.findViewById(R.id.listname);
		Button editLine = (Button)view.findViewById(R.id.editline);
		Button deleteLine = (Button)view.findViewById(R.id.deleteline);

		listHeader.setText(cursor.getString(LIST_HEADER_INDEX));

		long id = cursor.getLong(ROWID_INDEX);
		editLine.setTag(id);
		deleteLine.setTag(id);

		OnClickListener ocl = new OnClickListener(){
			@Override
			public void onClick(View v){
				Toast t = Toast.makeText(context, v.getTag().toString(), 
					Toast.LENGTH_SHORT);
				
				t.show();
			}
		};
		
		editLine.setOnClickListener(ocl);
		deleteLine.setOnClickListener(ocl);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.list_item_e, null);
	}


}
