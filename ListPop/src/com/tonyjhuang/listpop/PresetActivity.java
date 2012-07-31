package com.tonyjhuang.listpop;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

@TargetApi(11)
public class PresetActivity extends Activity {
	private Spinner listType;
	private Button finish;
	// Custom Spinner selection listener.
	CustomOnItemSelectedListener coisl = new CustomOnItemSelectedListener();
	FragmentManager fm = getFragmentManager();
	EditText etitle;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.presets);

		// Get Spinner handle and attach custom listener.
		listType = (Spinner) findViewById(R.id.presetspinner);
		listType.setOnItemSelectedListener(coisl);

		// Get finish button handle and attach OnClickListener.
		finish = (Button) findViewById(R.id.finishbutton);
		hookUpFinish();

		// EditText handle.
		etitle = (EditText) findViewById(R.id.presettitle);
	}

	// Depending on which Fragment is active, call the appropriate finish
	// method.
	private void hookUpFinish() {
		finish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment currentFragment;

				switch (listType.getSelectedItemPosition()) {
				case CustomOnItemSelectedListener.NUMBER_RANGE_SPINNER_INDEX:
					currentFragment = (NumberRangeFragment) fm
							.findFragmentById(R.id.fragmentframe);

					checkNumberFinale((NumberRangeFragment) currentFragment);
					break;

				case CustomOnItemSelectedListener.LETTER_RANGE_SPINNER_INDEX:
					currentFragment = (LetterRangeFragment) fm
							.findFragmentById(R.id.fragmentframe);

					checkLetterFinale((LetterRangeFragment) currentFragment);
					break;

				case CustomOnItemSelectedListener.YES_NO_SPINNER_INDEX:
					currentFragment = (YesNoFragment) fm
							.findFragmentById(R.id.fragmentframe);

					checkYesNoFinale((YesNoFragment) currentFragment);
					break;

				}
			}
		});
	}

	// Check if the range is valid (low < high). If not, Toast. If so,
	// create intent with extras and finish activity.
	private void checkNumberFinale(NumberRangeFragment nrf) {
		int low = nrf.getLowerBoundPicker().getValue();
		int high = nrf.getUpperBoundPicker().getValue();
		String title = getListTitle();

		if (low > high)
			alertToInvalidRange();
		else {
			Intent i = new Intent();
			String t;
			if (title == null || title.equals("")) {
				t = String.valueOf(low) + " to " + String.valueOf(high);
			} else {
				t = title;
			}
			List result = new List(t, indexToArray(low, high));
			i.putExtra(DbAdapter2.LIST, result.toString());
			setResult(2, i);
			finish();
		}
	}

	// Create ArrayList of numbers from low to high converted to Strings.
	private ArrayList<String> indexToArray(int low, int high) {
		ArrayList<String> a = new ArrayList<String>();
		for (int i = low; i < (high + 1); i++) {
			a.add(String.valueOf(i));
		}
		return a;
	}

	// Check if range is valid (low < high). If not, toast. If so,
	// check title. If it exists, create intent w/extras and finish activity.
	// If not, create the default title and finish.
	private void checkLetterFinale(LetterRangeFragment lrf) {
		String low = lrf.getLowerBoundSpinner().getSelectedItem().toString();
		String high = lrf.getUpperBoundSpinner().getSelectedItem().toString();

		String title = getListTitle();

		if (low.compareTo(high) > 0) {
			alertToInvalidRange();
		} else {
			Intent i = new Intent();

			char _low = low.charAt(0);
			char _Low = Character.toUpperCase(_low);

			char _high = high.charAt(0);
			char _High = Character.toUpperCase(_high);

			if (title == null || title.equals("")) {
				i.putExtra("list_header", _Low + " to " + _High);
			} else {
				i.putExtra("list_header", title);
			}

			ArrayList<String> array = indexCharToArray(_low, _high);
			i.putStringArrayListExtra("list", array);
			setResult(2, i);
			finish();
		}
	}

	// Create ArrayList of letters from low to high through ascii codes.
	private ArrayList<String> indexCharToArray(char low, char high) {
		int _low = (int) low;
		int _high = (int) high;

		ArrayList<String> array = new ArrayList<String>();

		for (int i = _low; i < (_high + 1); i++) {
			char c = (char) i;
			String s = Character.toString(c);
			array.add(s);
		}

		return array;

	}

	// Check title. If it exists, create intent w/extras and finish activity.
	// If not, create the default title and finish.
	private void checkYesNoFinale(YesNoFragment ynf) {
		String title = getListTitle();

		ArrayList<String> ynArray = new ArrayList<String>();
		ynArray.add("Yes");
		ynArray.add("No");

		Intent i = new Intent();

		if (title == null || title.equals("")) {
			i.putExtra("list_header", "Yes or No");
		} else {
			i.putExtra("list_header", title);
		}

		i.putStringArrayListExtra("list", ynArray);
		setResult(2, i);
		finish();
	}

	// Create toast alerting user to an invalid range.
	private void alertToInvalidRange() {
		Toast.makeText(PresetActivity.this, "invalid range!",
				Toast.LENGTH_SHORT).show();
	}

	// Get list title from EditText view.
	private String getListTitle() {
		return etitle.getText().toString();
	}

	// My implementation of OnItemSelectedListener for the Fragment preset
	// Spinner.
	private class CustomOnItemSelectedListener implements
			OnItemSelectedListener {
		private static final int NUMBER_RANGE_SPINNER_INDEX = 0;
		private static final int LETTER_RANGE_SPINNER_INDEX = 1;
		private static final int YES_NO_SPINNER_INDEX = 2;

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			FragmentManager fragmentManager = PresetActivity.this
					.getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();

			Fragment newFragment = null;

			switch (position) {
			case NUMBER_RANGE_SPINNER_INDEX:
				newFragment = new NumberRangeFragment();
				break;
			case LETTER_RANGE_SPINNER_INDEX:
				newFragment = new LetterRangeFragment();
				break;
			case YES_NO_SPINNER_INDEX:
				newFragment = new YesNoFragment();
				break;
			}
			fragmentTransaction.replace(R.id.fragmentframe, newFragment);
			fragmentTransaction.commit();

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}

	}
}
