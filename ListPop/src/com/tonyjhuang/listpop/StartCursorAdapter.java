package com.tonyjhuang.listpop;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class StartCursorAdapter extends CursorAdapter {
    private final LayoutInflater mInflater;

    public StartCursorAdapter(Context context, Cursor cursor) {
      super(context, cursor, true);
      mInflater = LayoutInflater.from(context);
    }

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		List currentList = new List(cursor.getString(cursor
				.getColumnIndex(DbAdapter2.LIST)));
		TextView t = (TextView) view.findViewById(R.id.listitem);
		
		t.setText(currentList.getTitle());
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.list_item, parent, false);
	}

}
