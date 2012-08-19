package com.tonyjhuang.listpop;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class TabsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs);
		ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ActionBar.Tab customTab = bar.newTab().setText("Custom");
		ActionBar.Tab presetTab = bar.newTab().setText("Preset");
		Fragment addFrag = new AddFrag();
		Fragment presFrag = new PresFrag();
		
		customTab.setTabListener(new MyTabsListener(addFrag));
		presetTab.setTabListener(new MyTabsListener(presFrag));
		
		bar.addTab(customTab);
		bar.addTab(presetTab);
	}

	protected class MyTabsListener implements ActionBar.TabListener {
		private Fragment fragment;

		public MyTabsListener(Fragment fragment) {
			this.fragment = fragment;
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.add(R.id.fragment_place, fragment, null);
		}

		@Override
		public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
			// TODO Auto-generated method stub

		}

		@SuppressLint("NewApi")
		@Override
		public void onTabUnselected(Tab arg0, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			ft.detach(fragment);
		}
	}
}
