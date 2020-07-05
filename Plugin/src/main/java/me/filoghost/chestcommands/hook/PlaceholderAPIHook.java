package me.filoghost.chestcommands.hook;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public enum PlaceholderAPIHook implements PluginHook {

	INSTANCE;
	
	private boolean enabled;

	@Override
	public void setup() {
		enabled = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public static boolean hasPlaceholders(String message) {
		INSTANCE.checkEnabledState();
		
		return PlaceholderAPI.containsPlaceholders(message);
	}

	public static String setPlaceholders(String message, Player viewer) {
		INSTANCE.checkEnabledState();

		return PlaceholderAPI.setPlaceholders(viewer, message);
	}

}
