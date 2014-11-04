package com.gmail.filoghost.chestcommands.internal.icon.command;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.gmail.filoghost.chestcommands.internal.icon.IconCommand;
import com.gmail.filoghost.chestcommands.util.Utils;

public class SoundIconCommand extends IconCommand {
	
	private Sound sound;
	private float pitch;
	private float volume;
	private String errorMessage;
	
	public SoundIconCommand(String command) {
		super(command);
		
		pitch = 1.0f;
		volume = 1.0f;
		
		String[] split = command.split(",");
		
		sound = Utils.matchSound(split[0]);
		if (sound == null) {
			errorMessage = ChatColor.RED + "Invalid sound \"" + split[0].trim() + "\".";
			return;
		}
		
		if (split.length > 1) {
			try {
				pitch = Float.parseFloat(split[1].trim());
			} catch (NumberFormatException e) {	}
		}
		
		if (split.length > 2) {
			try {
				volume = Float.parseFloat(split[2].trim());
			} catch (NumberFormatException e) {	}
		}
	}

	@Override
	public void execute(Player player) {
		if (errorMessage != null) {
			player.sendMessage(errorMessage);
			return;
		}
		
		player.playSound(player.getLocation(), sound, volume, pitch);
	}

}
