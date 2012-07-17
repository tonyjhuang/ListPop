package com.tonyjhuang.listpop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
//import android.content.Intent;

public class StartActivity extends Activity {
	
	Button chooseButton;
	private DbAdapter mDbA;
	ListView mListView;
	
	
    @SuppressLint("ParserError")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        
        //Start SQLite database;
        mDbA = new DbAdapter(this);
        mDbA.open();
        
        chooseButton = (Button)findViewById(R.id.chooseButton);
        hookUpChoose();
    }

    
    
    //Attaches onClickListener to chooseButton
    private void hookUpChoose(){
    	chooseButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		setContentView(R.layout.choose);
        		fillData();
        	}
        });
    }
    
    
    @SuppressWarnings("deprecation")
	private void fillData() {
        Cursor listCursor = mDbA.fetchAll();
        if(listCursor != null){
        startManagingCursor(listCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{DbAdapter.KEY_LIST_HEADER_COLUMN};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.listitem};

        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter aa = 
            new SimpleCursorAdapter(this, R.layout.list_item, listCursor, from, to);
       
        mListView.setAdapter(aa);
        }
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
