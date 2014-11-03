package com.gmail.filoghost.chestcommands.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.gmail.filoghost.chestcommands.ChestCommands;
import com.gmail.filoghost.chestcommands.Permissions;

public class JoinListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		if (ChestCommands.getLastReloadErrors() > 0 && event.getPlayer().hasPermission(Permissions.SEE_ERRORS)) {
			event.getPlayer().sendMessage(ChestCommands.CHAT_PREFIX + ChatColor.RED + "The plugin found " + ChestCommands.getLastReloadErrors() + " error(s) last time it was loaded. You can see them by doing \"/cc reload\" in the console.");
		}
		
		if (ChestCommands.hasNewVersion() && ChestCommands.getSettings().update_notifications && event.getPlayer().hasPermission(Permissions.UPDATE_NOTIFICATIONS)) {
			event.getPlayer().sendMessage(ChestCommands.CHAT_PREFIX + "Found an update: " + ChestCommands.getNewVersion() + ". Download:");
			event.getPlayer().sendMessage(ChatColor.DARK_GREEN + ">> " + ChatColor.GREEN + "http://dev.bukkit.org/bukkit-plugins/chest-commands");
		}
	}
	
}
