/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.api;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface ClickHandler {

	/**
	 * @param player the player that clicked on the inventory
	 * @return true if the inventory should be closed, false otherwise
	 */
	ClickResult onClick(MenuInventory menuInventory, Player player);

}
