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
package me.filoghost.chestcommands.parsing;

public class NumberParser {
	
	public static double getStrictlyPositiveDouble(String input) throws ParseException {
		return getStrictlyPositiveDouble(input, "number must be a valid decimal and greater than 0");
	}

	public static double getStrictlyPositiveDouble(String input, String errorMessage) throws ParseException {
		double value = getDouble(input, errorMessage);
		check(value > 0.0, errorMessage);
		return value;
	}

	private static double getDouble(String input, String errorMessage) throws ParseException {
		try {
			return Double.parseDouble(input);
		} catch (NumberFormatException ex) {
			throw new ParseException(errorMessage);
		}
	}
	
	public static short getPositiveShort(String input, String errorMessage) throws ParseException {
		short value = getShort(input, errorMessage);
		check(value >= 0, errorMessage);
		return value;
	}

	public static short getShort(String input, String errorMessage) throws ParseException {
		try {
			return Short.parseShort(input);
		} catch (NumberFormatException ex) {
			throw new ParseException(errorMessage);
		}
	}
	
	public static int getStrictlyPositiveInteger(String input) throws ParseException {
		return getStrictlyPositiveInteger(input, "number must be a valid integer and greater than 0");
	}

	public static int getStrictlyPositiveInteger(String input, String errorMessage) throws ParseException {
		int value = getInteger(input, errorMessage);
		check(value > 0, errorMessage);
		return value;
	}

	public static int getInteger(String input, String errorMessage) throws ParseException {
		try {
			return Integer.parseInt(input);
		} catch (NumberFormatException ex) {
			throw new ParseException(errorMessage);
		}
	}
	
	private static void check(boolean expression, String errorMessage) throws ParseException {
		if (!expression) {
			throw new ParseException(errorMessage);
		}
	}

}
