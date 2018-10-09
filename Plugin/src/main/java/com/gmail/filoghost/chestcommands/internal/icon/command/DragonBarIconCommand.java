package com.gmail.filoghost.chestcommands.internal.icon.command;

import org.bukkit.entity.Player;

import com.gmail.filoghost.chestcommands.bridge.BarAPIBridge;
import com.gmail.filoghost.chestcommands.internal.icon.IconCommand;
import com.gmail.filoghost.chestcommands.util.Utils;

public class DragonBarIconCommand extends IconCommand {

	private String message;
	private int seconds;
	
	public DragonBarIconCommand(String command) {
		super(command);
		
		seconds = 1;
		message = command;
		
		String[] split = command.split("\\|", 2); // Max of 2 pieces.
		if (split.length > 1 && Utils.isValidPositiveInteger(split[0].trim())) {
			seconds = Integer.parseInt(split[0].trim());
			message = split[1].trim();
		}
		
		message = Utils.addColors(message);
	}

	@Override
	public void execute(Player player) {
		if (BarAPIBridge.hasValidPlugin()) {
			BarAPIBridge.setMessage(player, message, seconds);
		}
	}

}
