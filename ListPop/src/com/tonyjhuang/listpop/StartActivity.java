package com.tonyjhuang.listpop;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class StartActivity extends Activity {
	
	private DbAdapter mDbA;
	ListView mListView;
	TextView mTextView;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);
        
        //Start SQLite database;
        mDbA = new DbAdapter(this);
        mDbA.open();
        //mDbA.enterList("HELLO!", new ArrayList<String>());
        mDbA.deleteAll();
        ArrayList<String> sampleArray = new ArrayList<String>();
        sampleArray.add("YO");
        sampleArray.add("TEST!");
        mDbA.enterList("WORLD!", sampleArray);
       
        mListView = (ListView)findViewById(R.id.listselection);
		hookUpItemClickListener();
		
		mTextView = (TextView)findViewById(R.id.textview);
		
		fillData();
    }

    
    private void fillData(){
		Cursor c = mDbA.fetchAll();
		startManagingCursor(c);
    	
    	String[] from = new String[]{DbAdapter.KEY_LIST_HEADER_COLUMN};
    	int[] to = new int[]{R.id.listitem};
    	SimpleCursorAdapter mAdapter =
    			new SimpleCursorAdapter(StartActivity.this, R.layout.list_item,
    					c, from, to);
    	mListView.setAdapter(mAdapter);
    }
    
    private void hookUpItemClickListener(){
    	mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> list, View view, int position,
					long id) {
				Intent i = new Intent(StartActivity.this, PopActivity.class);
				i.putExtra(DbAdapter.KEY_ROWID, id);
				startActivityForResult(i, 0);
				
			}
    	});
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	mDbA.close();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }
}

    
	
