/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.api;

import me.filoghost.chestcommands.api.internal.BackendAPI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Menus are containers of {@link Icon}s that can be displayed to players as unmodifiable inventories.
 * <p>
 * It is not recommended to implement this interface, use the provided constructor {@link Menu#create(Plugin, String,
 * int)}.
 *
 * @since 1
 */
public interface Menu {

    /**
     * Creates a new menu.
     *
     * @param owner    the plugin creating the menu
     * @param title    title of the menu that will be displayed in the inventory
     * @param rowCount number of rows in the menu (the number of columns is always 9, currently)
     * @return the created menu
     * @since 1
     */
    static @NotNull Menu create(@NotNull Plugin owner, @NotNull String title, int rowCount) {
        return BackendAPI.getImplementation().createMenu(owner, title, rowCount);
    }

    /**
     * Sets the icon in a given position, overriding the previous one if present.
     *
     * @param row    the row position
     * @param column the column position
     * @param icon   the new icon, null to remove the current one
     * @since 1
     */
    void setIcon(int row, int column, @Nullable Icon icon);

    /**
     * Returns the icon in a given position.
     *
     * @param row    the row position
     * @param column the column position
     * @return the icon at the give position, null if absent
     * @since 1
     */
    @Nullable Icon getIcon(int row, int column);

    /**
     * Returns the title of the displayed inventory.
     *
     * @return the title
     * @since 1
     */
    @NotNull String getTitle();

    /**
     * Returns the amount of rows of the displayed inventory.
     *
     * @return the amount of rows
     * @since 1
     */
    int getRowCount();

    /**
     * Returns the amount of columns of the displayed inventory.
     *
     * @return the amount of columns
     * @since 1
     */
    int getColumnCount();

    /**
     * Displays the menu to a player, creating a rendering of this menu and its icons.
     * <p>
     * If icons are added, removed or changed after the menu is displayed to a player, the view is not updated
     * automatically and you may want to invoke {@link Menu#refreshOpenMenuViews()}.
     *
     * @param player the player to which the menu will be displayed
     * @return the newly created view for the player
     * @since 1
     */
    @NotNull MenuView open(@NotNull Player player);

    /**
     * Refreshes all the menu views currently open and visible by players, reflecting the changes in the icons of this
     * menu and updating placeholders.
     * <p>
     * This method should be called after adding, removing or changing one or more icons to update the open menu views
     * of players.
     * <p>
     * This method invokes {@link MenuView#refresh()} on each open view created by this menu.
     *
     * @since 1
     */
    void refreshOpenMenuViews();

}