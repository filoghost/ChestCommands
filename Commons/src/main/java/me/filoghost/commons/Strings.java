/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.commons;

public final class Strings {

	
	public static String[] trimmedSplit(String string, String pattern) {
		return trimmedSplit(string, pattern, 0);
	}
	
	
	public static String[] trimmedSplit(String string, String pattern, int limit) {
		String[] output = string.split(pattern, limit);
		for (int i = 0; i < output.length; i++) {
			output[i] = output[i].trim();
		}
		return output;
	}
	

	public static String stripChars(String string, char... charsToRemove) {
		if (isEmpty(string) || charsToRemove.length == 0) {
			return string;
		}

		StringBuilder result = new StringBuilder(string.length());

		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			if (!arrayContains(charsToRemove, c)) {
				result.append(c);
			}
		}

		return result.toString();
	}

	private static boolean arrayContains(char[] array, char valueToFind) {
		for (char c : array) {
			if (c == valueToFind) {
				return true;
			}
		}

		return false;
	}

	public static String capitalizeFully(String string) {
		if (isEmpty(string)) {
			return string;
		}

		string = string.toLowerCase();
		int length = string.length();
		StringBuilder result = new StringBuilder(length);
		boolean capitalizeNext = true;

		for (int i = 0; i < length; i++) {
			char c = string.charAt(i);

			if (Character.isWhitespace(c)) {
				result.append(c);
				capitalizeNext = true;
			} else if (capitalizeNext) {
				result.append(Character.toTitleCase(c));
				capitalizeNext = false;
			} else {
				result.append(c);
			}
		}
		return result.toString();
	}

	public static String capitalizeFirst(String string) {
		if (isEmpty(string)) {
			return string;
		}

		return Character.toTitleCase(string.charAt(0)) + string.substring(1);
	}

	public static boolean isEmpty(String string) {
		return string == null || string.isEmpty();
	}
}
