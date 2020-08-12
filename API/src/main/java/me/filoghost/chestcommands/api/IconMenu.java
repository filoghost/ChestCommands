/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.api;

import me.filoghost.chestcommands.api.internal.BackendAPI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface IconMenu {
	
	static IconMenu create(Plugin owner, String title, int rowCount) {
		return BackendAPI.getImplementation().createIconMenu(owner, title, rowCount);
	}

	void setIcon(int row, int column, Icon icon);

	Icon getIcon(int row, int column);

	String getTitle();
	
	int getRowCount();
	
	int getColumnCount();

	/**
	 * Opens a view of the current menu configuration.
	 * Updating the menu doesn't automatically update all the views.
	 *
	 * @param player the player to which the menu will be displayed
	 */
	MenuInventory open(Player player);

	void refreshOpenInventories();

}