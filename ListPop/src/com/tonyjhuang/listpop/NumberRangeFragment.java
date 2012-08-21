package com.tonyjhuang.listpop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NumberRangeFragment extends Fragment {
	NumberPicker lower, upper;

	private static final int LOWEST_INDEX = 0;
	private static final int HIGHEST_INDEX = 99;
	private static final int UPPER_START_INDEX = 10;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.number_range, container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// Initialize UI references.
		lower = (NumberPicker) view.findViewById(R.id.lowerindex);
		upper = (NumberPicker) view.findViewById(R.id.upperindex);

		// Set both pickers' lower and upper bounds,
		// as well as their starting indices.
		lower.setRange(LOWEST_INDEX, HIGHEST_INDEX);
		upper.setRange(LOWEST_INDEX, HIGHEST_INDEX);

		lower.setCurrent(LOWEST_INDEX);
		upper.setCurrent(UPPER_START_INDEX);
	}

	public NumberPicker getLowerBoundPicker() {
		return lower;
	}

	public NumberPicker getUpperBoundPicker() {
		return upper;
	}
}
