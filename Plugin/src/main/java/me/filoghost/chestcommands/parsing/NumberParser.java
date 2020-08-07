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
package me.filoghost.chestcommands.parsing;

import me.filoghost.chestcommands.logging.ErrorMessages;

public class NumberParser {

	public static double getStrictlyPositiveDouble(String input) throws ParseException {
		double value = getDouble(input);
		check(value > 0.0, ErrorMessages.Parsing.strictlyPositive);
		return value;
	}

	private static double getDouble(String input) throws ParseException {
		try {
			return Double.parseDouble(input);
		} catch (NumberFormatException ex) {
			throw new ParseException(ErrorMessages.Parsing.invalidDecimal);
		}
	}

	public static float getFloat(String input) throws ParseException {
		try {
			return Float.parseFloat(input);
		} catch (NumberFormatException ex) {
			throw new ParseException(ErrorMessages.Parsing.invalidDecimal);
		}
	}

	public static short getPositiveShort(String input) throws ParseException {
		short value = getShort(input);
		check(value >= 0, ErrorMessages.Parsing.zeroOrPositive);
		return value;
	}

	private static short getShort(String input) throws ParseException {
		try {
			return Short.parseShort(input);
		} catch (NumberFormatException ex) {
			throw new ParseException(ErrorMessages.Parsing.invalidShort);
		}
	}

	public static int getStrictlyPositiveInteger(String input) throws ParseException {
		int value = getInteger(input);
		check(value > 0, ErrorMessages.Parsing.strictlyPositive);
		return value;
	}

	public static int getInteger(String input) throws ParseException {
		try {
			return Integer.parseInt(input);
		} catch (NumberFormatException ex) {
			throw new ParseException(ErrorMessages.Parsing.invalidInteger);
		}
	}

	private static void check(boolean expression, String errorMessage) throws ParseException {
		if (!expression) {
			throw new ParseException(errorMessage);
		}
	}
}
