package com.tonyjhuang.listpop;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Spinner;

public class PresetActivity extends Activity {
	private Spinner listType;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presets);
        
        listType = (Spinner)findViewById(R.id.presetspinner);
        listType.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}
}
