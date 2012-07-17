package com.tonyjhuang.listpop;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PopActivity extends Activity{
	//ArrayList<String> toBeDisplayed = 
			//getIntent().getStringArrayListExtra("arraylist"); 
	@SuppressLint("ParserError")
	private ArrayAdapter<String> aa;
	private ListView mListView;
	
	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.pop);
	        
	        //mListView = (ListView)findViewById(R.id.listview);
	        /*
	        ArrayList<String> array1 = new ArrayList<String>();
	    	array1.add("a");
	    	array1.add("b");
	    	array1.add("c");
	        
	        aa = new ArrayAdapter<String>(this, 
	        		R.layout.list_item,
	        		new ArrayList<String>());
	        */
	        //mListView.setAdapter(aa);
	 }
	 /*
	private String randomString(ArrayList<String> a){
    	Random randomGenerator = new Random();
    	int index = randomGenerator.nextInt(a.size());
    	return a.get(index);
    }
	*/
	
	
}
