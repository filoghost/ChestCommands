package me.filoghost.chestcommands.bridge;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlaceholderAPIBridge {

	private static PlaceholderAPIPlugin placeholderAPI;

	public static boolean setupPlugin() {
		Plugin placeholderPlugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");

		if (placeholderPlugin == null) {
			return false;
		}

		placeholderAPI = (PlaceholderAPIPlugin) placeholderPlugin;
		return true;
	}

	public static boolean hasValidPlugin() {
		return placeholderAPI != null;
	}

	public static boolean hasPlaceholders(String message) {
		if (!hasValidPlugin()) {
			throw new IllegalStateException("PlaceholderAPI plugin was not found!");
		}

		return PlaceholderAPI.containsPlaceholders(message);
	}

	public static String setPlaceholders(String message, Player executor) {
		if (!hasValidPlugin()) {
			throw new IllegalStateException("PlaceholderAPI plugin was not found!");
		}

		return PlaceholderAPI.setPlaceholders(executor, message);
	}

}
