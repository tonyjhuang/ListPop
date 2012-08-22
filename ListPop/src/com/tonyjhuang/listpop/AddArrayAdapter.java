package com.tonyjhuang.listpop;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class AddArrayAdapter extends BaseAdapter {
	private static final String TAG = "AddArrayAdapter";

	private ArrayList<String> list;
	private LayoutInflater mInflater;
	private ImageButton delete;
	private TextView itemText;
	private int layout;
	private AddArrayContainer context;

	public AddArrayAdapter(Context _context, int _layout) {
		super();

		// Create LayoutInflater in constructor to avoid asking for a new one
		// everytime in GetView.
		mInflater = LayoutInflater.from(_context);

		// this.context = context;
		list = new ArrayList<String>();
		layout = _layout;
		context = (AddArrayContainer) _context;
	}

	public AddArrayAdapter(Context _context, int _layout,
			ArrayList<String> _list) {
		super();

		mInflater = LayoutInflater.from(_context);
		layout = _layout;
		list = _list;
		context = (AddArrayContainer) _context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = mInflater.inflate(this.layout, null);

		itemText = (TextView) view.findViewById(R.id.listitem);
		delete = (ImageButton) view.findViewById(R.id.deleteline);
		delete.setTag(position);

		String s = list.get(position).toString();
		itemText.setText(s);

		Log.d(TAG, "Position of view = " + position);
		Log.d(TAG, "HELLO!");
		delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "Position of view clicked : " + v.getTag());
				Log.d(TAG, "attempting deleteFromAdapter...");
				Log.d(TAG, "Tag = " + v.getTag());
				Log.d(TAG, "Current Time = " + System.currentTimeMillis());
				Log.d(TAG, String.valueOf(context == null));
				context.deleteFromAdapter((Integer) v.getTag());
				Log.d(TAG, "deleteFromAdapter call successful!");
			}
		});

		return view;
	}

	// Add new String to end of ArrayList.
	public void add(String s) {
		list.add(0, s);
		this.notifyDataSetChanged();
	}
	
	public void addAll(Collection<String> c){
		list.addAll(0, c);
		this.notifyDataSetChanged();
	}

	// Remove index from ArrayList.
	public void remove(int pos) {
		list.remove(pos);
		this.notifyDataSetChanged();
	}

	public ArrayList<String> getList() {
		return list;
	}

	// ## Overriden methods ##
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int pos) {
		return list.get(pos);
	}

	public long getItemId(int pos) {
		return pos;
	}
}
