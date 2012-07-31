package com.tonyjhuang.listpop;

import java.util.ArrayList;
import java.util.Collections;

public class List {

	// data.get(0) = title.
	// data.get(1-...) = list.
	private ArrayList<String> data;

	public List(String title, ArrayList<String> array) {
		data = new ArrayList<String>();
		data.add(title);
		data.addAll(array);
	}
	
	public List(String cData) {
		ArrayList<String> data = new ArrayList<String>();
		String current = cData;

		int nextIndex = current.indexOf("|");

		while (current.length() != 0) {
			data.add(current.substring(0, nextIndex));
			current = current.substring(nextIndex + 1);
			nextIndex = current.indexOf("|");
		}
	}

	public String getTitle() {
		return data.get(0);
	}

	public ArrayList<String> getList() {
		ArrayList<String> result = data;
		result.remove(0);
		return result;
	}

	public String toString() {
		ArrayList<String> result = data;
		
		// Grab title string before operation begins.
		String title = result.get(0);
		result.remove(0);
		
		// Iterate over ArrayList.
		String s = "";
		Collections.reverse(result);
		for (String i : result) {
			s = i + "|" + s;
		}
		
		//Return the resulting string with the title added in front.
		return title + "|" + s;
		
	}

}
