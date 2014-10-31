package com.gmail.filoghost.chestcommands.internal;

import java.util.List;

import org.bukkit.entity.Player;

import com.gmail.filoghost.chestcommands.api.ClickHandler;
import com.gmail.filoghost.chestcommands.internal.icon.IconCommand;

public class CommandsClickHandler implements ClickHandler {

	private List<IconCommand> commands;
	
	public CommandsClickHandler(List<IconCommand> commands) {
		this.commands = commands;
	}
	
	@Override
	public void onClick(Player player) {
		if (commands != null && commands.size() > 0) {
			for (IconCommand command : commands) {
				command.execute(player);
			}
		}	
	}

	
	
}
