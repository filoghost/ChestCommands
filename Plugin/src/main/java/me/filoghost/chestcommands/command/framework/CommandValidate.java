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
package me.filoghost.chestcommands.command.framework;

public class CommandValidate {

	public static void notNull(Object o, String msg) {
		if (o == null) {
			throw new CommandException(msg);
		}
	}

	public static void isTrue(boolean b, String msg) {
		if (!b) {
			throw new CommandException(msg);
		}
	}

	public static int getPositiveInteger(String input) {
		try {
			int i = Integer.parseInt(input);
			if (i < 0) {
				throw new CommandException("The number must be 0 or positive.");
			}
			return i;
		} catch (NumberFormatException e) {
			throw new CommandException("Invalid number \"" + input + "\".");
		}
	}

	public static int getPositiveIntegerNotZero(String input) {
		try {
			int i = Integer.parseInt(input);
			if (i <= 0) {
				throw new CommandException("The number must be positive.");
			}
			return i;
		} catch (NumberFormatException e) {
			throw new CommandException("Invalid number \"" + input + "\".");
		}
	}

	public static double getPositiveDouble(String input) {
		try {
			double d = Double.parseDouble(input);
			if (d < 0) {
				throw new CommandException("The number must be 0 or positive.");
			}
			return d;
		} catch (NumberFormatException e) {
			throw new CommandException("Invalid number \"" + input + "\".");
		}
	}

	public static double getPositiveDoubleNotZero(String input) {
		try {
			double d = Integer.parseInt(input);
			if (d <= 0) {
				throw new CommandException("The number must be positive.");
			}
			return d;
		} catch (NumberFormatException e) {
			throw new CommandException("Invalid number \"" + input + "\".");
		}
	}

	public static void minLength(Object[] array, int minLength, String msg) {
		if (array.length < minLength) {
			throw new CommandException(msg);
		}
	}

}