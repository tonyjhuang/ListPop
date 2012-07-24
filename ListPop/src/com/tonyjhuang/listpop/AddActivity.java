package com.tonyjhuang.listpop;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class AddActivity extends Activity {
	private static final int DELETE_ID = Menu.FIRST;
	EditText listName, listEntry;
	ListView currentEntries;
	Button finishButton;
	ArrayList<String> list;
	ArrayAdapter<String> aa;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        
        listName = (EditText) findViewById(R.id.listnamecreation);
        listEntry = (EditText) findViewById(R.id.listitemcreation);
        hookUpListEntry();
        finishButton = (Button) findViewById(R.id.finishbutton);
        hookUpFinishButton();
        currentEntries = (ListView) findViewById(R.id.newitems);
        registerForContextMenu(currentEntries);
        
        list = new ArrayList<String>();
        
        aa = new ArrayAdapter<String>(this, R.layout.list_item, list);
        currentEntries.setAdapter(aa);
	}
	
	private void hookUpListEntry(){
		listEntry.setOnKeyListener(new OnKeyListener(){
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(event.getAction() == KeyEvent.ACTION_DOWN){
					switch(keyCode){
					case KeyEvent.KEYCODE_ENTER:
						String currentText = listEntry.getText().toString();
						list.add(0, currentText);
						listEntry.setText("");
						aa.notifyDataSetChanged();
						return true;
					default:
						break;
					}
				}
				
				return false;
			}
			
		});
	}
	
	private void hookUpFinishButton(){
		finishButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if(listName.getText().toString().equals("")){
					listName.setError(getString(R.string.no_header));
				} else if(list.size() == 0) {
					listEntry.setError(getString(R.string.no_items));
				}
				else {
				
					Intent i = new Intent();
					i.putExtra("list_header", listName.getText().toString());
					i.putStringArrayListExtra("list", list);
					setResult(1, i);
					finish();
				}
			}
		});
	}
	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
                list.remove(info.position);
				aa.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }
	
	@Override
	public void onBackPressed(){
		setResult(0);
		finish();
	}
	
}
