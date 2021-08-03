/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * A menu view is a rendering of a menu and its icons, made of an {@link Inventory} and {@link ItemStack}s, which can be
 * displayed to a player. Modifying the menu doesn't automatically refresh all the views.
 * <p>
 * Each menu view is linked only to one player, since icons may contain player-relative placeholders.
 * <p>
 * A new view is created each time a menu is displayed to a player with {@link Menu#open(Player)}.
 *
 * @since 1
 */
public interface MenuView {

    /**
     * Refreshes the current view by re-rendering icons, reflecting the changes in the menu and updating placeholders.
     * <p>
     * For example, this method should be called inside a {@link ClickHandler} if it changes the amount of money of the
     * player and you want to refresh the money placeholder instantly.
     * <p>
     * Note that {@link ClickHandler} exposes the menu view being interacted with, so you don't need to refresh all the
     * views of a menu through {@link Menu#refreshOpenViews()}.
     *
     * @since 1
     */
    void refresh();

    /**
     * Closes the current view for the viewer, if not already closed.
     *
     * @since 1
     */
    void close();

    /**
     * Returns the player linked to this view.
     *
     * @return the player
     * @since 1
     */
    @NotNull Player getViewer();

    /**
     * Returns the menu that generated this view.
     *
     * @return the menu
     * @since 1
     */
    @NotNull Menu getMenu();

}
