package com.tonyjhuang.listpop;

import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
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
	//private ArrayList<String> a = new ArrayList<String>();
	private TextView debugCount;
	private Cursor cIndex;
	Button nextButton;
	private TextView debugTitle;
	
	// 'start' or 'choose'.
	String status;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        
        //Start SQLite database;
        mDbA = new DbAdapter(this);
        mDbA.open();
        
        chooseButton = (Button)findViewById(R.id.chooseButton);
        hookUpChoose();
        status = "start";
        
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
        		status = "choose";
        		
        		debugCount = (TextView)findViewById(R.id.debugcounttextview);
                debugCount.setText(Long.toString(mDbA.fetchNumberOfLists()));
                
                debugTitle = (TextView)findViewById(R.id.debugtitletextview);
                
                nextButton = (Button)findViewById(R.id.nextbutton);
                hookUpNext();
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
    
    private void debugInitialize() throws JSONException{
    	mDbA.deleteAll();
    	mDbA.enterList("list 1", new ArrayList<String>());
    	mDbA.enterList("list 2", new ArrayList<String>());
    }
    
    private void debugNewText(){
    	String title = 
				cIndex.getString(cIndex.getColumnIndexOrThrow(
						DbAdapter.KEY_LIST_HEADER));
		debugTitle.setText(title);
		cIndex.moveToNext();
    }
    
    private void debugDB(){
    	cIndex = mDbA.debugFetch();
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
