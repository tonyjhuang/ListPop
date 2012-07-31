package com.tonyjhuang.listpop;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

public class LetterRangeFragment extends Fragment {
	Spinner lower, upper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.letter_range, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// Initialize UI references.
		lower = (Spinner) view.findViewById(R.id.lowerindex);
		upper = (Spinner) view.findViewById(R.id.upperindex);
	}

	public Spinner getLowerBoundSpinner() {
		return lower;
	}

	public Spinner getUpperBoundSpinner() {
		return upper;
	}
}
