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
package me.filoghost.chestcommands.util.collection;

public class ArrayGrid<T> extends Grid<T> {

	private final T[] elements;

	@SuppressWarnings("unchecked")
	public ArrayGrid(int rows, int columns) {
		super(rows, columns);
		this.elements = (T[]) new Object[getSize()];
	}

	@Override
	protected T getByIndex0(int ordinalIndex) {
		return elements[ordinalIndex];
	}

	@Override
	protected void setByIndex0(int ordinalIndex, T element) {
		elements[ordinalIndex] = element;
	}

}
