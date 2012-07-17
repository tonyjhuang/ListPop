package com.tonyjhuang.listpop;

import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class StartActivity extends Activity {
	
	Button chooseButton;
	private DbAdapter mDbA;
	private TextView debugCount;
	private Cursor cIndex;
	Button nextButton;
	private TextView debugTitle;
	Button moveToPop;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        
        //Start SQLite database;
        mDbA = new DbAdapter(this);
        mDbA.open();
        
        chooseButton = (Button)findViewById(R.id.chooseButton);
        hookUpChoose();
        
        cIndex = null;
        
        try {
			debugInitialize();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

    //Attaches onClickListener to chooseButton
    private void hookUpChoose(){
    	chooseButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		setContentView(R.layout.choose);
        		
        		debugCount = (TextView)findViewById(R.id.debugcounttextview);
                debugCount.setText(Long.toString(mDbA.fetchNumberOfLists()));
                
                debugTitle = (TextView)findViewById(R.id.debugtitletextview);
                
                nextButton = (Button)findViewById(R.id.nextbutton);
                hookUpNext();
                
                moveToPop = (Button)findViewById(R.id.popbutton);
                hookUpPop();
        	}
        });
    }
    
    private void hookUpNext(){
    	nextButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if((cIndex == null) || (cIndex.isAfterLast())){
					debugDB();
				}
				debugNewText();
			}
    	});
    }
    
    @SuppressLint("ParserError")
	private void hookUpPop(){
    	moveToPop.setOnClickListener(new OnClickListener(){
    		@Override
    		public void onClick(View v) {
    			int KEY_ROWID_COLUMN_INDEX = cIndex.getColumnIndexOrThrow(
    					DbAdapter.KEY_ROWID);
    			int rowid = cIndex.getInt(KEY_ROWID_COLUMN_INDEX);
    			long _rowid = Long.valueOf(rowid);
    			
    			
    			ArrayList<String> currentArray = 
    					fetchArrayListByRowId(_rowid);
    			Intent goToPop = new Intent(StartActivity.this, PopActivity.class);
    			goToPop.putStringArrayListExtra("arraylist", currentArray);
    			StartActivity.this.startActivity(goToPop);
    		}
    	});
    }
    
    private ArrayList<String> fetchArrayListByRowId(long rowid){
    	try {
			return mDbA.fetchArrayListByRowId(rowid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    
    private void debugInitialize() throws SQLException, IOException{
    	mDbA.deleteAll();
    	
    	ArrayList<String> array1 = new ArrayList<String>();
    	array1.add("a");
    	array1.add("b");
    	array1.add("c");
    	
    	
    	ArrayList<String> array2 = new ArrayList<String>();
    	array2.add("1");
    	array2.add("2");
    	array2.add("3");
    	
    	ArrayList<String> array3 = new ArrayList<String>();
    	array2.add("x");
    	array2.add("y");
    	array2.add("z");
    	
    	mDbA.enterList("list 1", array1);
    	mDbA.enterList("list 2", array2);
    	mDbA.enterList("LIST THREE", array3);
    }
    
    private void debugNewText(){
    	String title = 
				cIndex.getString(cIndex.getColumnIndexOrThrow(
						DbAdapter.KEY_LIST_HEADER_COLUMN));
		debugTitle.setText(title);
		cIndex.moveToNext();
    }
    
    private void debugDB(){
    	cIndex = mDbA.fetchAll();
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
