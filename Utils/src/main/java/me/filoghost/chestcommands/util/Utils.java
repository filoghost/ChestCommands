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

public class Utils {

	public static boolean isClassLoaded(String name) {
		try {
			Class.forName(name);
			return true;
		} catch (Throwable t) {
			return false;
		}
	}

	public static String formatEnum(Enum<?> enumValue) {
		return Strings.capitalizeFully(enumValue.name().replace("_", " "));
	}

	public static String addYamlExtension(String fileName) {
		if (fileName == null) {
			return null;
		}
		if (fileName.toLowerCase().endsWith(".yml")) {
			return fileName;
		} else {
			return fileName + ".yml";
		}
	}

}
