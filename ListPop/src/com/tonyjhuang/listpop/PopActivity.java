package com.tonyjhuang.listpop;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;

public class PopActivity extends Activity{
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.pop);
	 }
	 
	private String randomString(ArrayList<String> a){
    	Random randomGenerator = new Random();
    	int index = randomGenerator.nextInt(a.size());
    	return a.get(index);
    }
	
}
