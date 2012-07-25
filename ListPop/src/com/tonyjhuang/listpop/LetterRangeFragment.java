package com.tonyjhuang.listpop;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

public class LetterRangeFragment extends Fragment {
	Spinner lower, upper;
	EditText listHeader;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.letter_range, container, false);
    }
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		lower = (Spinner) view.findViewById(R.id.lowerindex);
		upper = (Spinner) view.findViewById(R.id.upperindex);
		
		listHeader = (EditText) view.findViewById(R.id.letterrangetitle);
	}
	
	public Spinner getLetterSpinner(String s){
		if(s.equals("lower")){
			return lower;
		} else if(s.equals("upper")){
			return upper;
		}
		return null;
	}
	
	public String getTitle(){
		return listHeader.getText().toString();
	}
	
}
