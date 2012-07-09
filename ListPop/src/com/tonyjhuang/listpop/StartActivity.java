package com.tonyjhuang.listpop;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartActivity extends Activity {
	
	Button chooseButton;
	private DbAdapter mDbA;
	
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
        
    }

    //Attaches onClickListener to chooseButton
    private void hookUpChoose(){
    	chooseButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		setContentView(R.layout.choose);
        		status = "choose";
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
