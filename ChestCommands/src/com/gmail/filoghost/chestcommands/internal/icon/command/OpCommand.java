package com.gmail.filoghost.chestcommands.internal.icon.command;

import org.bukkit.entity.Player;

import com.gmail.filoghost.chestcommands.internal.icon.IconCommand;

public class OpCommand extends IconCommand {

	public OpCommand(String command) {
		super(command);
	}

	@Override
	public void execute(Player player) {
		
		if (player.isOp()) {
			player.chat("/" + getParsedCommand(player));
			
		} else {
			player.setOp(true);
			player.chat("/" + getParsedCommand(player));
        	player.setOp(false);
		}
	}

}
