/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.chestcommands.util;

public final class Strings {

	private Strings() {}


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
