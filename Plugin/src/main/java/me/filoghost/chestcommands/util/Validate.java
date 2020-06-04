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

public final class Validate {

	private Validate() {
	}

	public static void notNull(Object object, String error) {
		if (object == null) {
			throw new NullPointerException(error);
		}
	}

	public static void isTrue(boolean statement, String error) {
		if (!statement) {
			throw new IllegalArgumentException(error);
		}
	}

	public static void isFalse(boolean statement, String error) {
		if (statement) {
			throw new IllegalArgumentException(error);
		}
	}

}
