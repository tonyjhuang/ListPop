package com.tonyjhuang.listpop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class NumberRangeFragment extends Fragment {
	EditText lower, upper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.number_range, container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// Initialize UI references.
		lower = (EditText) view.findViewById(R.id.lowerindex);
		upper = (EditText) view.findViewById(R.id.upperindex);
		
		
	}

	public int getLowerBound() {
		String lb = lower.getText().toString();
		// If there is no input
		if(lb.equals("")){
			lower.setError(getString(R.string.no_lower_bound));
			return 100;
		} else if (Integer.parseInt(lb) < 0 || Integer.parseInt(lb) > 99){
			lower.setError(getString(R.string.invalid_number));
			return 100;
		} else
			return Integer.parseInt(lb);
	}

	public int getUpperBound() {
		String ub = upper.getText().toString();
		// If there is no input
		if(ub.equals("")){
			upper.setError(getString(R.string.no_upper_bound));
			return 100;
		} else if (Integer.parseInt(ub) < 0 || Integer.parseInt(ub) > 99){
			upper.setError(getString(R.string.invalid_number));
			return 100;
		} else
			return Integer.parseInt(ub);
	}
}
