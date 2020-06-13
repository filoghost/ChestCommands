package me.filoghost.chestcommands.util;

import org.bukkit.plugin.Plugin;

public final class FileUtils {
	

	private FileUtils() {}
	

	public static void saveResourceSafe(Plugin plugin, String name) {
		try {
			plugin.saveResource(name, false);
		} catch (Exception ignored) {}
	}
	
}
