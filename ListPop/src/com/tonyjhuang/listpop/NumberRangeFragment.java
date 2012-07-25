package com.tonyjhuang.listpop;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

public class NumberRangeFragment extends Fragment{
	NumberPicker lower, upper;
	
	private static final int LOWEST_INDEX = 0;
	private static final int HIGHEST_INDEX = 99;
	private static final int UPPER_START_POINT = 10;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.number_range, container, false);
        return view;
    }
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		lower = (NumberPicker) view.findViewById(R.id.lowerindex);
		upper = (NumberPicker) view.findViewById(R.id.upperindex);
		
		lower.setMinValue(LOWEST_INDEX);
		upper.setMinValue(LOWEST_INDEX);
		
		lower.setMaxValue(HIGHEST_INDEX);
		upper.setMaxValue(HIGHEST_INDEX);
		
		lower.setValue(LOWEST_INDEX);
		upper.setValue(UPPER_START_POINT);
	}
	
	public NumberPicker getNumberPicker(String s){
		if(s.equals("lower")){
			return lower;
		} else if(s.equals("upper")){
			return upper;
		}
		return null;
	}
}
