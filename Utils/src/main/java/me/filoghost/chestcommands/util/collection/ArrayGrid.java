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
