package com.gmail.filoghost.chestcommands.bridge;

import me.confuser.barapi.BarAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BarAPIBridge {

	private static BarAPI barAPI;
	
	public static boolean setupPlugin() {
		 Plugin barPlugin = Bukkit.getPluginManager().getPlugin("BarAPI");
				 
		 if (barPlugin == null) {
			 return false;
	     }
		 
		 barAPI = (BarAPI) barPlugin;
		 return true;
	}
	
	public static boolean hasValidPlugin() {
		return barAPI != null;
	}
		
	public static void setMessage(Player player, String message, int seconds) {
		if (!hasValidPlugin()) throw new IllegalStateException("BarAPI plugin was not found!");
		
		BarAPI.setMessage(player, message, seconds);
	}
	
}
