package com.gmail.filoghost.chestcommands.internal.icon.command;

import org.bukkit.entity.Player;

import com.gmail.filoghost.chestcommands.bridge.bungee.BungeeCordUtils;
import com.gmail.filoghost.chestcommands.internal.icon.IconCommand;

public class ServerCommand extends IconCommand {

	public ServerCommand(String command) {
		super(command);
	}

	@Override
	public void execute(Player player) {
		BungeeCordUtils.connect(player, command);
	}

}
