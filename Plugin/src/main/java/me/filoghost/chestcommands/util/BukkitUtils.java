package me.filoghost.chestcommands.util;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;

public final class BukkitUtils {

	private static final Method LEGACY_GET_ONLINE_PLAYERS;

	static {
		try {
			Method method = Bukkit.class.getDeclaredMethod("getOnlinePlayers");
			LEGACY_GET_ONLINE_PLAYERS = method.getReturnType() == Player[].class ? method : null;
		} catch (NoSuchMethodException e) {
			// This should NEVER happen!
			throw new IllegalStateException("Missing Bukkit.getOnlinePlayers() method!");
		}
	}

	private BukkitUtils() {
	}

	public static Collection<? extends Player> getOnlinePlayers() {
		try {
			if (LEGACY_GET_ONLINE_PLAYERS == null) {
				return Bukkit.getOnlinePlayers();
			} else {
				Player[] playersArray = (Player[]) LEGACY_GET_ONLINE_PLAYERS.invoke(null);
				return ImmutableList.copyOf(playersArray);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

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
