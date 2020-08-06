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

	
	public static String[] trimmedSplit(String input, String pattern) {
		return trimmedSplit(input, pattern, 0);
	}
	
	
	public static String[] trimmedSplit(String input, String pattern, int limit) {
		String[] output = input.split(pattern, limit);
		for (int i = 0; i < output.length; i++) {
			output[i] = output[i].trim();
		}
		return output;
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
		if (input == null) {
			return null;
		}

		String s = input.toLowerCase();

		int strLen = s.length();
		StringBuilder output = new StringBuilder(strLen);
		boolean capitalizeNext = true;
		for (int i = 0; i < strLen; i++) {
			char ch = s.charAt(i);

			if (Character.isWhitespace(ch)) {
				output.append(ch);
				capitalizeNext = true;
			} else if (capitalizeNext) {
				output.append(Character.toTitleCase(ch));
				capitalizeNext = false;
			} else {
				output.append(ch);
			}
		}
		return output.toString();
	}

	public static String capitalizeFirst(String input) {
		if (isEmpty(input)) {
			return input;
		}

		return Character.toTitleCase(input.charAt(0)) + input.substring(1);
	}

	public static boolean isEmpty(String s) {
		return s == null || s.isEmpty();
	}
}
