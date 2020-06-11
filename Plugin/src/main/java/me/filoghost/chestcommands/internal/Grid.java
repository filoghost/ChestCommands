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
package me.filoghost.chestcommands.internal;

import me.filoghost.chestcommands.util.Preconditions;

/* 
 * Example:
 * There 3 rows and 9 columns. The number inside the cells is the index.
 * 
 *                           <--- Column --->
 *                  
 *               0    1    2    3    4    5    6    7    8
 *    ^       +--------------------------------------------+
 *    |     0 |  0 |  1 |  2 |  3 |  4 |  5 |  6 |  7 |  8 |
 *            |----+----+----+----+----+----+----+----+----|
 *   Row    1 |  9 | 10 | 11 | 12 | 13 | 14 | 15 | 16 | 17 |
 *            |----+----+----+----+----+----+----+----+----|
 *    |     2 | 18 | 19 | 20 | 21 | 22 | 23 | 24 | 25 | 26 | 
 *    v       +--------------------------------------------+
 * 
 */
public class Grid<T> {
	
	private final int rows, columns;
	private final T[] elements;


	@SuppressWarnings("unchecked")
	public Grid(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		elements = (T[]) new Object[rows * columns];
	}

	public void setElement(int x, int y, T element) {
		elements[getOrdinalIndex(x, y)] = element;
	}

	public T getElement(int x, int y) {
		return getElementAtIndex(getOrdinalIndex(x, y));
	}
	
	public T getElementAtIndex(int ordinalIndex) {
	    Preconditions.checkIndex(ordinalIndex, getSize(), "ordinalIndex");
		return elements[ordinalIndex];
	}

	private int getOrdinalIndex(int x, int y) {
	    Preconditions.checkIndex(x, getColumns(), "x");
	    Preconditions.checkIndex(y, getRows(), "y");
	    
		int ordinalIndex = y * getColumns() + x;
		Preconditions.checkIndex(ordinalIndex, getSize(), "ordinalIndex");
		return ordinalIndex;
	}
	
	public int getRows() {
		return rows;
	}
	
	public int getColumns() {
		return columns;
	}

	public int getSize() {
		return elements.length;
	}

}
