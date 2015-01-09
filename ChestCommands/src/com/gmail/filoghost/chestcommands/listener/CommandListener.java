package com.gmail.filoghost.chestcommands.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.gmail.filoghost.chestcommands.ChestCommands;
import com.gmail.filoghost.chestcommands.internal.ExtendedIconMenu;
import com.gmail.filoghost.chestcommands.util.StringUtils;

public class CommandListener implements Listener {

	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onCommand(PlayerCommandPreprocessEvent event) {
	
		if (ChestCommands.getSettings().use_only_commands_without_args && event.getMessage().contains(" ")) {
			return;
		}
		
		// Very fast method compared to split & substring.
		String command = StringUtils.getCleanCommand(event.getMessage());
		
		if (command.isEmpty()) {
			return;
		}
		
		ExtendedIconMenu menu = ChestCommands.getCommandToMenuMap().get(command);
		
		if (menu != null) {
			event.setCancelled(true);
			
			if (event.getPlayer().hasPermission(menu.getPermission())) {
				menu.open(event.getPlayer());
			} else {
				menu.sendNoPermissionMessage(event.getPlayer());
			}
		}
	}
	
}
