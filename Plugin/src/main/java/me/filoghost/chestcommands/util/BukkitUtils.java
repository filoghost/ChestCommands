package me.filoghost.chestcommands.util;

import org.bukkit.Sound;
import org.bukkit.plugin.Plugin;

public final class BukkitUtils {
	

	private BukkitUtils() {}
	

	public static String addYamlExtension(String input) {
		if (input == null) {
			return null;
		}
		return input.toLowerCase().endsWith(".yml") ? input : input + ".yml";
	}

	public static void saveResourceSafe(Plugin plugin, String name) {
		try {
			plugin.saveResource(name, false);
		} catch (Exception ignored) {
		}
	}

	public static Sound matchSound(String input) {
		if (input == null) {
			return null;
		}

		input = StringUtils.stripChars(input.toLowerCase(), " _-");

		for (Sound sound : Sound.values()) {
			if (StringUtils.stripChars(sound.toString().toLowerCase(), "_").equals(input)) {
				return sound;
			}
		}
		return null;
	}

}
