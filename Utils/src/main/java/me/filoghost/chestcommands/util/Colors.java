package me.filoghost.chestcommands.util;

import org.bukkit.ChatColor;

public final class Colors {

	public static String addColors(String input) {
		if (input == null || input.isEmpty()) {
			return input;
		}
		return ChatColor.translateAlternateColorCodes('&', input);
	}

}
