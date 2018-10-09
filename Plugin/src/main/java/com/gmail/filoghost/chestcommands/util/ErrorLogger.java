package com.gmail.filoghost.chestcommands.util;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a class to collect all the errors found while loading the plugin.
 */
public class ErrorLogger {

	private List<String> errors = new ArrayList<String>();
	
	public void addError(String error) {
		errors.add(error);
	}
	
	public List<String> getErrors() {
		return new ArrayList<String>(errors);
	}

	public boolean hasErrors() {
		return errors.size() > 0;
	}
	
	public int getSize() {
		return errors.size();
	}
	
}
