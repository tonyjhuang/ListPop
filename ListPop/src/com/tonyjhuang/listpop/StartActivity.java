package com.tonyjhuang.listpop;

import java.util.ArrayList;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
//import android.content.Intent;

public class StartActivity extends Activity {
	
	Button chooseButton;
	private DbAdapter mDbA;
	ListView mListView;
	TextView mTextView;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        
        //Start SQLite database;
        mDbA = new DbAdapter(this);
        mDbA.open();
        //mDbA.enterList("HELLO!", new ArrayList<String>());
        mDbA.deleteAll();
        ArrayList<String> sampleArray = new ArrayList<String>();
        sampleArray.add("YO");
        mDbA.enterList("WORLD!", sampleArray);
       
        
        chooseButton = (Button)findViewById(R.id.chooseButton);
        hookUpChoose();
    }

    
    
    //Attaches onClickListener to chooseButton
    private void hookUpChoose(){
    	chooseButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		setContentView(R.layout.choose);
        		
        		mListView = (ListView)findViewById(R.id.listselection);
        		mTextView = (TextView)findViewById(R.id.textview);
        		
        		ArrayList<String> list = new ArrayList<String>();
        		list.add("item 1");
        		list.add("item 2");
        		list.add("item 3");
        		ArrayAdapter<String> aa = new ArrayAdapter<String>(StartActivity.this, 
                								R.layout.list_item, list);
        		//mListView.setAdapter(aa);
        		
        		Cursor c = mDbA.fetchAll();
        		startManagingCursor(c);
            	
            	String[] from = new String[]{DbAdapter.KEY_LIST_COLUMN};
            	int[] to = new int[]{R.id.listitem};
            	SimpleCursorAdapter mAdapter =
            			new SimpleCursorAdapter(StartActivity.this, R.layout.list_item,
            					c, from, to);
            	mListView.setAdapter(mAdapter);
        	}
        });
    }
    
    
    
    @Override
    public void onResume(){
    	super.onResume();
    	mDbA.open();
    }
    
    @Override
    public void onStop(){
    	super.onStop();
    	mDbA.close();
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

    
	
