/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.api;

import me.filoghost.chestcommands.api.internal.BackendAPI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ChestCommandsAPI {
	
	
	private ChestCommandsAPI() {}
	
	
	/**
	 * The API version is increased every time the API is modified.
	 * You can use it to require a minimum version, as features may
	 * be added (rarely removed) in future versions.
	 * 
	 * @return the API version
	 */
	public static int getAPIVersion() {
		return 1;
	}


	public static void registerPlaceholder(Plugin plugin, String identifier, PlaceholderReplacer placeholderReplacer) {
		BackendAPI.getImplementation().registerPlaceholder(plugin, identifier, placeholderReplacer);
	}
	

	/**
	 * Checks if a menu with a given file name was loaded by the plugin.
	 *
	 * @return if the menu was found
	 */
	public static boolean isPluginMenu(String yamlFile) {
		return BackendAPI.getImplementation().isPluginMenu(yamlFile);
	}
	

	/**
	 * Opens a menu loaded by ChestCommands to a player.
	 * NOTE: this method ignores permissions.
	 *
	 * @param player the player that will see the menu
	 * @param yamlFile the file name of the menu to open (with the .yml extension)
	 * @return if the menu was found and opened
	 */
	public static boolean openPluginMenu(Player player, String yamlFile) {
		return BackendAPI.getImplementation().openPluginMenu(player, yamlFile);
	}
}
