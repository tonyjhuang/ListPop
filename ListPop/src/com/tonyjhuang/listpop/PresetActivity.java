package com.tonyjhuang.listpop;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;

@TargetApi(11)
public class PresetActivity extends Activity {
	private Spinner listType;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presets);
        
        listType = (Spinner)findViewById(R.id.presetspinner);
        listType.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}
	
	private class CustomOnItemSelectedListener implements OnItemSelectedListener {
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
				FragmentManager fragmentManager = 
					PresetActivity.this.getFragmentManager();
				FragmentTransaction fragmentTransaction = 
						fragmentManager.beginTransaction();
				
				Fragment newFragment = new LetterRangeFragment();
				
				fragmentTransaction.add(R.id.fragmentframe, newFragment);
				fragmentTransaction.commit();
			case YES_NO_SPINNER_INDEX:
				break;
			}
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}

	}
}
