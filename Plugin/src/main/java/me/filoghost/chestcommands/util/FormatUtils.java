package me.filoghost.chestcommands.util;

import org.bukkit.ChatColor;

import me.filoghost.chestcommands.ChestCommands;

import java.util.List;

public final class FormatUtils {

	public static String addColors(String input) {
		if (input == null || input.isEmpty()) return input;
		return ChatColor.translateAlternateColorCodes('&', input);
	}

	public static String colorizeName(String input) {
		if (input == null || input.isEmpty()) return input;

		if (input.charAt(0) != ChatColor.COLOR_CHAR) {
			return ChestCommands.getSettings().default_color__name + addColors(input);
		} else {
			return addColors(input);
		}
	}

	public static List<String> colorizeLore(List<String> input) {
		if (input == null || input.isEmpty()) return input;

		for (int i = 0; i < input.size(); i++) {

			String line = input.get(i);

			if (line.isEmpty()) continue;

			if (line.charAt(0) != ChatColor.COLOR_CHAR) {
				input.set(i, ChestCommands.getSettings().default_color__lore + addColors(line));
			} else {
				input.set(i, addColors(line));
			}
		}
		return input;
	}
}
