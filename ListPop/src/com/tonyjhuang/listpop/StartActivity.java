package com.tonyjhuang.listpop;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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
		} catch (JSONException e) {
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
    
    private void hookUpPop(){
    	moveToPop.setOnClickListener(new OnClickListener(){
    		@Override
    		public void onClick(View v) {
    			try {
					ArrayList<String> a = fetchArrayListByRowId(cIndex.getColumnIndexOrThrow(DbAdapter.KEY_LIST_COLUMN));
					debugCount.setText(a.get(1));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    			//Intent goToPop = new Intent(StartActivity.this, PopActivity.class);
    			//goToPop.putStringArrayListExtra("arraylist", currentArray);
    			//StartActivity.this.startActivity(goToPop);
    		}
    	});
    }
    
    private ArrayList<String> fetchArrayListByRowId(long rowid) throws JSONException{
    	String list = 
				cIndex.getString(cIndex.getColumnIndexOrThrow(
						DbAdapter.KEY_LIST_COLUMN));
    	JSONObject json = new JSONObject(list);
    	JSONArray items = json.optJSONArray("array");
    	
    	ArrayList<String> currentArray = new ArrayList<String>();
    	for(int i=0; i<items.length(); i++){
    		currentArray.add(items.getString(i));
    	}
    	return currentArray;
    }
    
    private void debugInitialize() throws JSONException{
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
