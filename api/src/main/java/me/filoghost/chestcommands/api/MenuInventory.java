/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.api;

import org.bukkit.entity.Player;

public interface MenuInventory {

	void refresh();

	Player getViewer();

	IconMenu getIconMenu();

}
