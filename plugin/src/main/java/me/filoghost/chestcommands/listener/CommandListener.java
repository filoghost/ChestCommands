/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.listener;

import me.filoghost.chestcommands.menu.InternalMenu;
import me.filoghost.chestcommands.menu.MenuManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {
	
	private final MenuManager menuManager;
	
	public CommandListener(MenuManager menuManager) {
		this.menuManager = menuManager;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		String command = getCommandName(event.getMessage());
		if (command == null) {
			return;
		}

		InternalMenu menu = menuManager.getMenuByOpenCommand(command);
		if (menu == null) {
			return;
		}
		
		event.setCancelled(true);
		menu.openCheckingPermission(event.getPlayer());
	}
	
	private static String getCommandName(String fullCommand) {
		if (!fullCommand.startsWith("/")) {
			return null;
		}
		
		int firstSpace = fullCommand.indexOf(' ');
		if (firstSpace >= 1) {
			return fullCommand.substring(1, firstSpace);
		} else {
			return fullCommand.substring(1);
		}
	}

}
