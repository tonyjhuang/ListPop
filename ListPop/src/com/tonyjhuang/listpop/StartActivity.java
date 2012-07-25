package com.tonyjhuang.listpop;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class StartActivity extends Activity {
	private static final int ADD_ACTIVITY = 1;
	private static final int PRESETS_ACTIVITY = ADD_ACTIVITY + 1;
	private static final int BACK_BUTTON_PRESSED = -1;
	private static final int DELETE_ID = Menu.FIRST;
	
	private DbAdapter mDbA;
	ListView mListView;
	TextView mTextView;
	Button add, presets, edit;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);
        
        //Start SQLite database;
        mDbA = new DbAdapter(this);
        mDbA.open();
       
        mListView = (ListView)findViewById(R.id.listselection);
		hookUpItemClickListener();
		registerForContextMenu(mListView);
		
		mTextView = (TextView)findViewById(R.id.textview);
		
		add = (Button)findViewById(R.id.addbutton);
		hookUpAdd();
		
		edit = (Button)findViewById(R.id.editbutton);
		hookUpEdit();
		
		presets = (Button)findViewById(R.id.presets);
		hookUpPresets();
		
		fillData();
    }
   
    private void hookUpItemClickListener(){
    	mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> list, View view, int position,
					long id) {
				Intent i = new Intent(StartActivity.this, PopActivity.class);
				i.putExtra(DbAdapter.KEY_ROWID, id);
				startActivity(i);
				
			}
    	});
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
                mDbA.deleteListItem(info.id);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }
       
    private void hookUpAdd(){
    	add.setOnClickListener(new OnClickListener(){
    		@Override
    		public void onClick(View v){
    			Intent i = new Intent(StartActivity.this, AddActivity.class);
    			startActivityForResult(i, ADD_ACTIVITY);
    		}
    	});
    }
    
    private void hookUpEdit(){
    	//TODO: How do we want to edit lists?
    }
    
    private void hookUpPresets(){
    	presets.setOnClickListener(new OnClickListener(){
    		@Override
    		public void onClick(View v){
    			Intent i = new Intent(StartActivity.this, PresetActivity.class);
    			startActivityForResult(i, PRESETS_ACTIVITY);
    		}
    	});
    }
    
    @SuppressWarnings("deprecation")
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
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	switch(resultCode){
    	case BACK_BUTTON_PRESSED:
    		mTextView.setText("BACKBUTTONPRESSED!");
    		break;
    		
    	case ADD_ACTIVITY:
    		
    	case PRESETS_ACTIVITY:
    		String newListHeader = data.getStringExtra("list_header");
    		ArrayList<String> newList = data.getStringArrayListExtra("list");
    		mDbA.enterList(newListHeader, newList);
    		
    		fillData();
    		break;
    	}
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

    
	
