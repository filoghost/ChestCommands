package me.filoghost.chestcommands.placeholder;

import org.bukkit.entity.Player;

import me.filoghost.chestcommands.hook.PlaceholderAPIHook;

public class PlaceholderManager {

	public static boolean hasPlaceholders(String message) {
		if(message == null) {
			return false;
		}
		for (Placeholder placeholder : Placeholder.values()) {
			if (message.contains(placeholder.getText())) {
				return true;
			}
		}
		if (PlaceholderAPIHook.INSTANCE.isEnabled() && PlaceholderAPIHook.hasPlaceholders(message)) {
			return true;
		}
		return false;
	}

	public static String replacePlaceholders(String message, Player viewer) {
		if (message == null) {
			return null;
		}
		for (Placeholder placeholder : Placeholder.values()) {
			if (message.contains(placeholder.getText())) {
				message = message.replace(placeholder.getText(), placeholder.getReplacement(viewer));
			}
		}
		if (PlaceholderAPIHook.INSTANCE.isEnabled()) {
			message = PlaceholderAPIHook.setPlaceholders(message, viewer);
		}
		return message;
	}

}
