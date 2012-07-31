package com.tonyjhuang.listpop;

import java.util.ArrayList;

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

	// private final Context context;
	private ArrayList<String> list;
	private LayoutInflater mInflater;
	private ImageButton delete;
	private TextView itemText;
	private int layout;

	public AddArrayAdapter(Context context, int _layout) {
		super();

		// Create LayoutInflater in constructor to avoid asking for a new one
		// everytime in GetView.
		mInflater = LayoutInflater.from(context);

		// this.context = context;
		list = new ArrayList<String>();
		layout = _layout;
	}
	
	public AddArrayAdapter(Context context, int _layout, ArrayList<String> _list){
		super();
		
		mInflater = LayoutInflater.from(context);
		layout = _layout;
		list = _list;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = mInflater.inflate(this.layout, null);

		itemText = (TextView) view.findViewById(R.id.listitem);
		delete = (ImageButton) view.findViewById(R.id.deleteline);
		
		String s = list.get(position).toString();
		itemText.setText(s);

		delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AddArrayAdapter.this.remove(position);
			}
		});

		return view;
	}

	// Add new String to end of ArrayList.
	public void add(String s) {
		list.add(s);
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

	//## Overriden methods ##
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int pos) {
		return list.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}
}
