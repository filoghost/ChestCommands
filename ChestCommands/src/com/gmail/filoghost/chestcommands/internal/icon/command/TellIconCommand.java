package com.gmail.filoghost.chestcommands.internal.icon.command;

import org.bukkit.entity.Player;

import com.gmail.filoghost.chestcommands.internal.icon.IconCommand;
import com.gmail.filoghost.chestcommands.util.Utils;

public class TellIconCommand extends IconCommand {

	public TellIconCommand(String command) {
		super(Utils.addColors(command));
	}

	@Override
	public void execute(Player player) {
		player.sendMessage(getParsedCommand(player));
	}

}
