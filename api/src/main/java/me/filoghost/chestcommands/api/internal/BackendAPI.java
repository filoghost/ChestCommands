/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.api.internal;

import me.filoghost.chestcommands.api.ConfigurableIcon;
import me.filoghost.chestcommands.api.Menu;
import me.filoghost.chestcommands.api.PlaceholderReplacer;
import me.filoghost.chestcommands.api.StaticIcon;
import me.filoghost.fcommons.Preconditions;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public abstract class BackendAPI {
	
	private static BackendAPI implementation;
	
	public static void setImplementation(BackendAPI implementation) {
		Preconditions.notNull(implementation, "implementation");
		Preconditions.checkState(BackendAPI.implementation == null, "implementation already set");

		BackendAPI.implementation = implementation;
	}
	
	public static BackendAPI getImplementation() {
		Preconditions.checkState(implementation != null, "no implementation set");
		
		return implementation;
	}
	
	public abstract boolean pluginMenuExists(String menuFileName);

	public abstract boolean openPluginMenu(Player player, String menuFileName);

	public abstract Menu createMenu(Plugin owner, String title, int rows);
	
	public abstract ConfigurableIcon createConfigurableIcon(Material material);

	public abstract StaticIcon createStaticIcon(ItemStack itemStack);

	public abstract void registerPlaceholder(Plugin plugin, String identifier, PlaceholderReplacer placeholderReplacer);

}
