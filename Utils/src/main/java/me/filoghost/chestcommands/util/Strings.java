/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.chestcommands.util;

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
