package com.gmail.filoghost.chestcommands.api;

import org.bukkit.entity.Player;

import com.gmail.filoghost.chestcommands.ChestCommands;

public class ChestCommandsAPI {
	
	/**
	 * Checks if a menu with a given file name was loaded by the plugin.
	 * 
	 * @return true - if the menu was found.
	 */
	public static boolean isPluginMenu(String yamlFile) {
		return ChestCommands.getFileNameToMenuMap().containsKey(yamlFile);
	}
	
	/**
	 * Opens a menu loaded by ChestCommands to a player.
	 * NOTE: this method ignores permissions.
	 * 
	 * @param player - the player that will see the GUI.
	 * @param yamlFile - the file name of the menu to open. The .yml extension CANNOT be omitted.
	 * @return true - if the menu was found and opened, false if not.
	 */
	public static boolean openPluginMenu(Player player, String yamlFile) {
		IconMenu menu = ChestCommands.getFileNameToMenuMap().get(yamlFile);
		
		if (menu != null) {
			menu.open(player);
			return true;
		} else {
			return false;
		}
	}
}
