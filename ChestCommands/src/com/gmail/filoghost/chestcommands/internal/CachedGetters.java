package com.gmail.filoghost.chestcommands.internal;

import java.lang.reflect.Method;
import java.util.Collection;

import org.bukkit.Bukkit;

public class CachedGetters {
	
	private static long lastOnlinePlayersRefresh;
	private static int onlinePlayers;
	private static Method getOnlinePlayersMethod;
	
	static {
		try {
			getOnlinePlayersMethod = Bukkit.class.getMethod("getOnlinePlayers");
		} catch (Exception ex) { }
	}

	public static int getOnlinePlayers() {
		long now = System.currentTimeMillis();
		if (lastOnlinePlayersRefresh == 0 || now - lastOnlinePlayersRefresh > 1000) {
			// getOnlinePlayers() could be expensive if called frequently
			lastOnlinePlayersRefresh = now;
			
			try {
				onlinePlayers = count(getOnlinePlayersMethod.invoke(null));
			} catch (Exception e) {
				onlinePlayers = -1;
			}
		}
		
		return onlinePlayers;
	}
	
	private static int count(Object o) {
		if (o.getClass().isArray()) {
			return ((Object[]) o).length;
		} else if (o instanceof Collection<?>) {
			return ((Collection<?>) o).size();
		} else {
			return -1;
		}
	}
	
}
