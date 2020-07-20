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
public abstract class Grid<T> {
	
	private final int rows, columns, size;

	public Grid(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		this.size = rows * columns;
	}

	public final void set(int row, int column, T element) {
		setByIndex(toOrdinalIndex(row, column), element);
	}

	public final T get(int row, int column) {
		return getByIndex(toOrdinalIndex(row, column));
	}
	
	public final T getByIndex(int ordinalIndex) {
	    Preconditions.checkIndex(ordinalIndex, getSize(), "ordinalIndex");
		return getByIndex0(ordinalIndex);
	}

	protected abstract T getByIndex0(int ordinalIndex);

	public final void setByIndex(int ordinalIndex, T element) {
		Preconditions.checkIndex(ordinalIndex, getSize(), "ordinalIndex");
		setByIndex0(ordinalIndex, element);
	}


	protected abstract void setByIndex0(int ordinalIndex, T element);

	private int toOrdinalIndex(int row, int column) {
	    Preconditions.checkIndex(row, getRows(), "row");
		Preconditions.checkIndex(column, getColumns(), "column");

		int ordinalIndex = row * getColumns() + column;
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
		return size;
	}

}
