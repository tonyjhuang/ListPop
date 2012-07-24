package com.tonyjhuang.listpop;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class CustomOnItemSelectedListener implements OnItemSelectedListener {
	private static final int NUMBER_RANGE_SPINNER_INDEX = 0;
	private static final int LETTER_RANGE_SPINNER_INDEX = 1;
	private static final int YES_NO_SPINNER_INDEX = 2;
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		switch(position){
		case NUMBER_RANGE_SPINNER_INDEX:
			Toast.makeText(parent.getContext(), 
					"OnItemSelectedListener : " + parent.
					getItemAtPosition(position).toString(),
					Toast.LENGTH_SHORT).show();
			break;
		case LETTER_RANGE_SPINNER_INDEX:
			break;
		case YES_NO_SPINNER_INDEX:
			break;
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
