/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.task;

import me.filoghost.chestcommands.inventory.DefaultMenuInventory;
import me.filoghost.chestcommands.menu.InternalIconMenu;
import me.filoghost.chestcommands.menu.MenuManager;
import me.filoghost.chestcommands.placeholder.PlaceholderManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TickingTask implements Runnable {

	private long currentTick;

	@Override
	public void run() {
		updateInventories();
		PlaceholderManager.onTick();

		currentTick++;
	}

	private void updateInventories() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			DefaultMenuInventory menuInventory = MenuManager.getOpenMenuInventory(player);

			if (menuInventory == null || !(menuInventory.getIconMenu() instanceof InternalIconMenu)) {
				continue;
			}

			int refreshTicks = ((InternalIconMenu) menuInventory.getIconMenu()).getRefreshTicks();

			if (refreshTicks > 0 && currentTick % refreshTicks == 0) {
				menuInventory.refresh();
			}
		}
	}

}
