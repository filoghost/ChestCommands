/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.menu;

import me.filoghost.chestcommands.api.Icon;
import me.filoghost.chestcommands.api.IconMenu;
import me.filoghost.chestcommands.api.MenuInventory;
import me.filoghost.chestcommands.inventory.ArrayGrid;
import me.filoghost.chestcommands.inventory.DefaultMenuInventory;
import me.filoghost.chestcommands.inventory.Grid;
import me.filoghost.commons.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class BaseIconMenu implements IconMenu {

    
	protected final String title;
	protected final Grid<Icon> icons;


	public BaseIconMenu(String title, int rows) {
		this.title = title;
		this.icons = new ArrayGrid<>(rows, 9);
	}

	@Override
	public void setIcon(int row, int column, Icon icon) {
		icons.set(row, column, icon);
	}

	@Override
	public Icon getIcon(int row, int column) {
		return icons.get(row, column);
	}

	@Override
	public int getRowCount() {
		return icons.getRows();
	}
	
	@Override
	public int getColumnCount() {
		return icons.getColumns();
	}

	@Override
	public String getTitle() {
		return title;
	}

	public Grid<Icon> getIcons() {
		return icons;
	}

	@Override
	public MenuInventory open(Player player) {
		Preconditions.notNull(player, "player");

		DefaultMenuInventory menuInventory = new DefaultMenuInventory(this, player);
		menuInventory.open(player);
		return menuInventory;
	}

	@Override
	public void refreshOpenInventories() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			DefaultMenuInventory menuInventory = MenuManager.getOpenMenuInventory(player);
			if (menuInventory != null && menuInventory.getIconMenu() == this) {
				menuInventory.refresh();
			}
		}
	}
}
