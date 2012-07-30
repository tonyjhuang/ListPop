package com.tonyjhuang.listpop;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

public class EditActivity extends Activity {
	ListView mListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);
		
		mListView = (ListView) findViewById(R.id.list);
		
		LayoutInflater inflater = getLayoutInflater();
		View listHeading = inflater.inflate(R.layout.edit_list_header, mListView, false);
		mListView.addHeaderView(listHeading);
	}
}
