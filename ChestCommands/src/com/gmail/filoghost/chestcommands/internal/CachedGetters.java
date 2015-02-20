package com.gmail.filoghost.chestcommands.internal;

import org.bukkit.Bukkit;

public class CachedGetters {
	
	private static long lastOnlinePlayersRefresh;
	private static int onlinePlayers;

	public static int getOnlinePlayers() {
		long now = System.currentTimeMillis();
		if (lastOnlinePlayersRefresh == 0 || now - lastOnlinePlayersRefresh > 1000) {
			// getOnlinePlayers() could be expensive if called frequently
			onlinePlayers = Bukkit.getOnlinePlayers().length;
		}
		
		return onlinePlayers;
	}
	
}
