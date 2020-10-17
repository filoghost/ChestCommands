/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.api;

import me.filoghost.chestcommands.api.internal.BackendAPI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Menus are containers of {@link Icon}s that can be displayed to players as unmodifiable inventories, organized as a
 * grid with a number of rows and columns.
 * <p>
 * This interface should not be implemented, use the provided constructor {@link Menu#create(Plugin, String, int)}. Any
 * custom implementation will not be handled by Chest Commands' event listener, which relies on internal details. New
 * methods may also be added, making existing custom implementations incompatible.
 *
 * @since 1
 */
@ApiStatus.NonExtendable
public interface Menu {

    /**
     * Creates a new menu.
     *
     * @param plugin the plugin creating the menu
     * @param title  title of the menu that appears in the displayed inventory
     * @param rows   number of rows in the menu (the number of columns is always 9, currently)
     * @return the created menu
     * @since 1
     */
    static @NotNull Menu create(@NotNull Plugin plugin, @NotNull String title, int rows) {
        return BackendAPI.getImplementation().createMenu(plugin, title, rows);
    }

    /**
     * Sets the icon in a given position, overriding the previous one if present.
     *
     * @param row    the row position
     * @param column the column position
     * @param icon   the new icon, null to remove the current one
     * @throws IndexOutOfBoundsException if the row or the column is outside the limits ({@code row < 0 || row >=
     *                                   getRows() || column < 0 || column >= getColumns()})
     * @since 1
     */
    void setIcon(int row, int column, @Nullable Icon icon);

    /**
     * Returns the icon in a given position.
     *
     * @param row    the row position
     * @param column the column position
     * @return the icon at the give position, null if absent
     * @throws IndexOutOfBoundsException if the row or the column is outside the limits ({@code row < 0 || row >=
     *                                   getRows() || column < 0 || column >= getColumns()})
     * @since 1
     */
    @Nullable Icon getIcon(int row, int column);

    /**
     * Displays the menu to a player, creating a rendering of this menu and its icons.
     * <p>
     * If icons are added, removed or changed after the menu is displayed to a player, the view is not updated
     * automatically and you may want to invoke {@link Menu#refreshOpenViews()}.
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
     * This method invokes {@link MenuView#refresh()} on each currently open view created by this menu.
     *
     * @since 1
     */
    void refreshOpenViews();

    /**
     * Returns the amount of rows of the displayed inventory.
     *
     * @return the amount of rows
     * @since 1
     */
    int getRows();

    /**
     * Returns the amount of columns of the displayed inventory.
     *
     * @return the amount of columns
     * @since 1
     */
    int getColumns();

    /**
     * Returns the title of the displayed inventory.
     *
     * @return the title
     * @since 1
     */
    @NotNull String getTitle();

    /**
     * Returns the plugin that created the menu.
     *
     * @return the plugin that created the menu
     * @since 1
     */
    Plugin getPlugin();

}