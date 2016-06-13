package com.gmail.filoghost.chestcommands.util;

public class StringUtils {

	public static String stripChars(String input, String removed) {
		if (removed == null || removed.isEmpty()) {
			return input;
		}
		
		return stripChars(input, removed.toCharArray());
	}
	
	// Removes the first slash, and returns the all the chars until a space is encontered.
	public static String getCleanCommand(String message) {
		char[] chars = message.toCharArray();
		
		if (chars.length <= 1) {
			return "";
		}
		
		int pos = 0;
		for (int i = 1; i < chars.length; i++) {
			if (chars[i] == ' ') {
				break;
			}
			
			chars[(pos++)] = chars[i];
		}
		
		return new String(chars, 0, pos);
	}
	
	public static String stripChars(String input, char... removed) {
		if (input == null || input.isEmpty() || removed.length == 0) {
			return input;
		}
		
		char[] chars = input.toCharArray();
		
	    int pos = 0;
	    for (int i = 0; i < chars.length; i++) {
	    	if (!arrayContains(removed, chars[i])) {
	    		chars[(pos++)] = chars[i];
	    	}
	    }
		
		return new String(chars, 0, pos);
	}
	
	private static boolean arrayContains(char[] arr, char match) {
		for (char c : arr) {
			if (c == match) {
				return true;
			}
		}
		
		return false;
	}
	
	public static String capitalizeFully(String input) {
		if (input == null) return null;
		
		String s = input.toLowerCase();
		
		int strLen = s.length();
	    StringBuffer buffer = new StringBuffer(strLen);
	    boolean capitalizeNext = true;
	    for (int i = 0; i < strLen; i++) {
	    	char ch = s.charAt(i);

	    	if (Character.isWhitespace(ch)) {
	    		buffer.append(ch);
	    		capitalizeNext = true;
	    	} else if (capitalizeNext) {
	    		buffer.append(Character.toTitleCase(ch));
	    		capitalizeNext = false;
	    	} else {
	    		buffer.append(ch);
	    	}
	    }
	    return buffer.toString();
	}
	
	public static boolean isNullOrEmpty(String s) {
		return s == null || s.isEmpty();
	}
	
}
