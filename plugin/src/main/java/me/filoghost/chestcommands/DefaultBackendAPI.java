/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands;

import me.filoghost.chestcommands.api.ConfigurableIcon;
import me.filoghost.chestcommands.api.Menu;
import me.filoghost.chestcommands.api.PlaceholderReplacer;
import me.filoghost.chestcommands.api.StaticIcon;
import me.filoghost.chestcommands.api.internal.BackendAPI;
import me.filoghost.chestcommands.icon.APIConfigurableIcon;
import me.filoghost.chestcommands.icon.APIStaticIcon;
import me.filoghost.chestcommands.menu.APIMenu;
import me.filoghost.chestcommands.menu.InternalMenu;
import me.filoghost.chestcommands.placeholder.PlaceholderManager;
import me.filoghost.fcommons.Preconditions;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class DefaultBackendAPI extends BackendAPI {

    @Override
    public boolean pluginMenuExists(String menuFileName) {
        Preconditions.notNull(menuFileName, "menuFileName");

        return ChestCommands.getMenuManager().getMenuByFileName(menuFileName) != null;
    }

    @Override
    public boolean openPluginMenu(Player player, String menuFileName) {
        Preconditions.notNull(player, "player");
        Preconditions.notNull(menuFileName, "menuFileName");

        InternalMenu menu = ChestCommands.getMenuManager().getMenuByFileName(menuFileName);

        if (menu != null) {
            menu.open(player);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ConfigurableIcon createConfigurableIcon(Material material) {
        return new APIConfigurableIcon(material);
    }

    @Override
    public Menu createMenu(Plugin owner, String title, int rows) {
        return new APIMenu(owner, title, rows);
    }

    @Override
    public StaticIcon createStaticIcon(ItemStack itemStack) {
        return new APIStaticIcon(itemStack);
    }

    @Override
    public void registerPlaceholder(Plugin plugin, String identifier, PlaceholderReplacer placeholderReplacer) {
        PlaceholderManager.registerPluginPlaceholder(plugin, identifier, placeholderReplacer);
    }

}
