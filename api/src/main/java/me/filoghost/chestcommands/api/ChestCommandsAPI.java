/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.api;

import me.filoghost.chestcommands.api.internal.BackendAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * The main entry point for the Chest Commands API.
 * <p>
 * To create a menu, start by looking at {@link Menu#create(Plugin, String, int)} and {@link
 * ConfigurableIcon#create(Material)}.
 *
 * @since 1
 */
public class ChestCommandsAPI {


    private ChestCommandsAPI() {}


    /**
     * Returns the API version number, which is increased every time the API changes. This number is used to check if a
     * certain method or class (which may have been added later) is present or not.
     * <p>
     * All public API classes and methods are documented with the Javadoc tag {@code @since}, indicating in which API
     * version that element was introduced.
     * <p>
     * You can use it to require a minimum version, as features may be added (rarely removed) in future versions. The
     * first version of the API is 1.
     *
     * @return the current API version
     * @since 1
     */
    public static int getAPIVersion() {
        return 1;
    }

    /**
     * Registers a placeholder that will be replaced by a custom value provided by the given placeholder replacer, which
     * may be different for each player.
     * <p>
     * Placeholders can be used in some parts of {@link ConfigurableIcon}, most notably the name and the lore, but they
     * are disabled by default. The method {@link ConfigurableIcon#setPlaceholdersEnabled(boolean)} must be invoked on
     * each icon which should have placeholders enabled.
     * <p>
     * Menus loaded by Chest Commands from the menus folder always display placeholders, including those registered
     * through this method.
     * <p>
     * The identifier is automatically converted to the appropriate placeholder format. For example, given the
     * identifier "test", the callback would be invoked to replace the the following placeholders:
     * <ul>
     *     <li>{test}
     *     <li>{test: 123}
     *     <li>{test: hello world}
     * </ul>
     *
     * @param plugin              the plugin registering the placeholder
     * @param identifier          the identifier of the placeholder, which can only contain letters, digits and
     *                            underscores
     * @param placeholderReplacer the callback that returns the displayed value
     * @see PlaceholderReplacer#getReplacement(Player, String)
     * @since 1
     */
    public static void registerPlaceholder(@NotNull Plugin plugin,
                                           @NotNull String identifier,
                                           @NotNull PlaceholderReplacer placeholderReplacer) {
        BackendAPI.getImplementation().registerPlaceholder(plugin, identifier, placeholderReplacer);
    }


    /**
     * Returns if a menu with a given file name exists and was loaded successfully by Chest Commands from the menus
     * folder.
     *
     * @param menuFileName the file name of the menu to check
     * @return true if the menu exists, false otherwise
     * @since 1
     */
    public static boolean pluginMenuExists(@NotNull String menuFileName) {
        return BackendAPI.getImplementation().pluginMenuExists(menuFileName);
    }


    /**
     * Opens a menu loaded by Chest Commands from the menus folder.
     * <p>
     * <b>WARNING</b>: this method opens the menu without checking the permissions of the player.
     *
     * @param player       the player that will see the menu
     * @param menuFileName the file name of the menu to open
     * @return true if the menu was found and opened successfully, false otherwise
     * @since 1
     */
    public static boolean openPluginMenu(@NotNull Player player, @NotNull String menuFileName) {
        return BackendAPI.getImplementation().openPluginMenu(player, menuFileName);
    }

}
