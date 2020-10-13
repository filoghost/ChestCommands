/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.inventory;

import org.jetbrains.annotations.Nullable;

public class ArrayGrid<T> extends Grid<T> {

    private final T[] elements;

    @SuppressWarnings("unchecked")
    public ArrayGrid(int rows, int columns) {
        super(rows, columns);
        this.elements = (T[]) new Object[getSize()];
    }

    @Override
    protected @Nullable T getByIndex0(int ordinalIndex) {
        return elements[ordinalIndex];
    }

    @Override
    protected void setByIndex0(int ordinalIndex, @Nullable T element) {
        elements[ordinalIndex] = element;
    }

}
