/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.inventory;

import me.filoghost.commons.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MenuInventoryHolder implements InventoryHolder {

	private final DefaultMenuInventory menuInventory;

	public MenuInventoryHolder(DefaultMenuInventory menuInventory) {
		Preconditions.notNull(menuInventory, "menuInventory");
		this.menuInventory = menuInventory;
	}

	@Override
	public Inventory getInventory() {
		/*
		 * This inventory will not do anything.
		 * I'm 90% sure that it doesn't break any other plugin,
		 * because the only way you can get here is using InventoryClickEvent,
		 * that is cancelled by ChestCommands, or using InventoryOpenEvent.
		 */
		return Bukkit.createInventory(null, 9);
	}

	public DefaultMenuInventory getMenuInventory() {
		return menuInventory;
	}
}
