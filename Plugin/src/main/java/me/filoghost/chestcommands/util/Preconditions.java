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

import java.util.Collection;

import org.bukkit.Material;

public final class Preconditions {

	private Preconditions() {}

	public static void notNull(Object object, String objectName) {
		if (object == null) {
			throw new NullPointerException(objectName + " cannot be null");
		}
	}
	
	public static void notEmpty(Collection<?> collection, String objectName) {
		notNull(collection, objectName);
		if (collection.isEmpty()) {
			throw new IllegalArgumentException(objectName + " cannot be empty");
		}
	}
	
	public static void checkArgument(boolean expression, String errorMessage) {
		if (!expression) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

	public static void checkState(boolean expression, String errorMessage) {
		if (!expression) {
			throw new IllegalStateException(errorMessage);
		}
	}

	public static void checkIndex(int index, int size, String objectName) {
		checkArgument(size >= 0, "size cannot be negative");
		
		if (index < 0) {
			throw new IndexOutOfBoundsException(objectName + " (" + index + ") cannot be negative");
		}
		if (index >= size) {
			throw new IndexOutOfBoundsException(objectName + " (" + index + ") must be less than size (" + size + ")");
		}
	}
	
	public static void checkArgumentNotAir(Material material, String objectName) {
		notNull(material, objectName);
		if (MaterialsHelper.isAir(material)) {
			throw new IllegalArgumentException(objectName + " cannot be " + material);
		}
	}

}
