package com.tonyjhuang.listpop;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class YesNoFragment extends Fragment{
	EditText listHeader;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.yes_no, container, false);
        return view;
    }
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		listHeader = (EditText) view.findViewById(R.id.yesnotitle);
	}
	
	public String getTitle(){
		return listHeader.getText().toString();
	}
}
