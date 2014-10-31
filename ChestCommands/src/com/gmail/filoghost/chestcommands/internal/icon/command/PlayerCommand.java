package com.gmail.filoghost.chestcommands.internal.icon.command;

import org.bukkit.entity.Player;

import com.gmail.filoghost.chestcommands.internal.icon.IconCommand;

public class PlayerCommand extends IconCommand {

	public PlayerCommand(String command) {
		super(command);
	}

	@Override
	public void execute(Player player) {
		player.chat('/' + getParsedCommand(player));
	}

}
