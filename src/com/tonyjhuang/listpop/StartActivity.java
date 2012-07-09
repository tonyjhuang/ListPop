package com.tonyjhuang.listpop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        
        Button chooseButton = (Button)findViewById(R.id.chooseButton);
        
        chooseButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		Intent gotoChooseList = new Intent(StartActivity.this, ChooseList.class);
        		StartActivity.this.startActivity(gotoChooseList);
        	}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    
}
