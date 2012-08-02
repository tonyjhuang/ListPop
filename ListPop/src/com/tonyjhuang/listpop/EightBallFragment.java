package com.tonyjhuang.listpop;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ListView;

public class EightBallFragment extends Fragment {
	ListView list;
	EditText add;
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
		add = (EditText) view.findViewById(R.id.additem);
		
		add.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_ENTER:
						String currentText = add.getText().toString();
						aa.add(currentText);
						add.setText("");
						return true;
					default:
						break;
					}
				}
				return false;
			}
		});

		ArrayList<String> eightBall = new ArrayList<String>();
		Collections.addAll(eightBall, getResources().getStringArray(R.array.eight_ball));
		
		aa = new AddArrayAdapter(getActivity(), R.layout.list_item_d, eightBall);
		list.setAdapter(aa);
	}
	
	public ArrayList<String> getList() {
		return aa.getList();
	}
}
