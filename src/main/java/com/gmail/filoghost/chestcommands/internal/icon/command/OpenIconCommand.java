package com.gmail.filoghost.chestcommands.internal.icon.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.gmail.filoghost.chestcommands.ChestCommands;
import com.gmail.filoghost.chestcommands.internal.ExtendedIconMenu;
import com.gmail.filoghost.chestcommands.internal.icon.IconCommand;

public class OpenIconCommand extends IconCommand {

	public OpenIconCommand(String command) {
		super(command);
	}

	@Override
	public void execute(final Player player) {
		final ExtendedIconMenu menu = ChestCommands.getFileNameToMenuMap().get(command.toLowerCase());
		if (menu != null) {
			
			// Delay the task, since this command is executed in ClickInventoryEvent
			// and opening another inventory in the same moment is not a good idea.
			Bukkit.getScheduler().scheduleSyncDelayedTask(ChestCommands.getInstance(), new Runnable() {
				public void run() {
					if (player.hasPermission(menu.getPermission())) {
						menu.open(player);
					} else {
						menu.sendNoPermissionMessage(player);
					}
				}
			});
			
		} else {
			player.sendMessage(ChatColor.RED + "Menu not found! Please inform the staff.");
		}
	}

}
