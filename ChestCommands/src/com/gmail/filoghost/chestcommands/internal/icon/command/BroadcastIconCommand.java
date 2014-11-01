package com.gmail.filoghost.chestcommands.internal.icon.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.gmail.filoghost.chestcommands.internal.icon.IconCommand;
import com.gmail.filoghost.chestcommands.util.Utils;

public class BroadcastIconCommand extends IconCommand {

	public BroadcastIconCommand(String command) {
		super(Utils.addColors(command));
	}

	@Override
	public void execute(Player player) {
		Bukkit.broadcastMessage(getParsedCommand(player));
		
	}

}
