package com.tonyjhuang.listpop;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class EightBallFragment extends Fragment {
	ListView list;
	AddArrayAdapter aa;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.eight_ball, container, false);
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// Initialize UI references.
		list = (ListView) view.findViewById(R.id.eightballlist);
		
		ArrayList<String> eightBall = new ArrayList<String>();
		Collections.addAll(eightBall, getResources().getStringArray(R.array.eight_ball));
		
		aa = new AddArrayAdapter(getActivity(), R.layout.list_item_d, eightBall);
		list.setAdapter(aa);
	}
}
