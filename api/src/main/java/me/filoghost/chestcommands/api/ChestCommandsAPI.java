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
     * The identifier is used to compute which placeholder formats will invoke the replacer callback. For example, given
     * the identifier "test", the callback would be invoked to replace the following placeholders (case insensitive):
     * <ul>
     *     <li>{test}
     *     <li>{test: argument}
     *     <li>{pluginName/test}
     *     <li>{pluginName/test: argument}
     *     <li>{PLUGINNAME/TEST: abc}
     *     <li>...
     * </ul>
     * The plugin name is used as optional namespace, to distinguish two placeholders with the same identifier but
     * registered by distinct plugins.
     * <p>
     * This method replaces any currently registered placeholder with the same plugin and identifier (case insensitive).
     *
     * @param plugin              the plugin registering the placeholder
     * @param identifier          the case-insensitive identifier of the placeholder, which can only contain letters,
     *                            digits and underscores; its length must be between 1 and 30 characters
     * @param placeholderReplacer the callback that returns the displayed value
     * @throws IllegalArgumentException if the identifier contains invalid characters, is too short or too long
     * @see PlaceholderReplacer#getReplacement(Player, String)
     * @since 1
     */
    public static void registerPlaceholder(@NotNull Plugin plugin,
                                           @NotNull String identifier,
                                           @NotNull PlaceholderReplacer placeholderReplacer) {
        BackendAPI.getImplementation().registerPlaceholder(plugin, identifier, placeholderReplacer);
    }

    /**
     * Unregisters a placeholder.
     *
     * @param plugin     the plugin that previously registered the placeholder
     * @param identifier the case-insensitive identifier of the placeholder, which can only contain letters, digits and
     *                   underscores; length must be between 1 and 30 characters
     * @return true if the placeholder existed and was removed, false otherwise
     * @throws IllegalArgumentException if the identifier contains invalid characters, is too short or too long
     * @since 1
     */
    public static boolean unregisterPlaceholder(@NotNull Plugin plugin, @NotNull String identifier) {
        return BackendAPI.getImplementation().unregisterPlaceholder(plugin, identifier);
    }

    /**
     * Returns if a menu with a given file name exists and was loaded successfully by Chest Commands from the menus
     * folder.
     *
     * @param menuFileName the file name of the menu to check, including the {@code .yml} file extension
     * @return true if the menu exists, false otherwise
     * @since 1
     */
    public static boolean isInternalMenuLoaded(@NotNull String menuFileName) {
        return BackendAPI.getImplementation().isInternalMenuLoaded(menuFileName);
    }


    /**
     * Opens a menu loaded by Chest Commands from the menus folder.
     * <p>
     * <b>WARNING</b>: this method opens the menu without checking the permissions of the player.
     *
     * @param player       the player that will see the menu
     * @param menuFileName the file name of the menu to open, including the {@code .yml} file extension
     * @return true if the menu was found and opened successfully, false otherwise
     * @since 1
     */
    public static boolean openInternalMenu(@NotNull Player player, @NotNull String menuFileName) {
        return BackendAPI.getImplementation().openInternalMenu(player, menuFileName);
    }

}
