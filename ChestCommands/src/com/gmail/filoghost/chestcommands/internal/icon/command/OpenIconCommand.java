package com.gmail.filoghost.chestcommands.internal.icon.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.gmail.filoghost.chestcommands.ChestCommands;
import com.gmail.filoghost.chestcommands.api.IconMenu;
import com.gmail.filoghost.chestcommands.internal.icon.IconCommand;

public class OpenIconCommand extends IconCommand {

	public OpenIconCommand(String command) {
		super(command);
	}

	@Override
	public void execute(Player player) {
		IconMenu menu = ChestCommands.getFileNameToMenuMap().get(command.toLowerCase());
		if (menu != null) {
			menu.open(player);
		} else {
			player.sendMessage(ChatColor.RED + "Menu not found! Please inform the staff.");
		}
	}

}
