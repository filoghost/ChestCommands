/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Common interface extended by other interfaces, represents a simplified {@link Icon} with a settable click handler.
 * <p>
 * This interface exists to avoid having to implement {@link Icon#onClick(MenuView, Player)} via subclassing.
 *
 * @see ConfigurableIcon
 * @see StaticIcon
 * @since 1
 */
public interface ClickableIcon extends Icon {

    /**
     * Sets the click handler for this icon.
     *
     * @param clickHandler the new click handler, null to remove
     * @since 1
     */
    void setClickHandler(@Nullable ClickHandler clickHandler);

    /**
     * Returns the current click handler.
     *
     * @return the current click handler, null if absent
     * @since 1
     */
    @Nullable ClickHandler getClickHandler();

    /**
     * {@inheritDoc}
     * <p>
     * This default implementation delegates the click event to the current click handler. This method should not be
     * overridden.
     *
     * @since 1
     */
    @Override
    default void onClick(@NotNull MenuView menuView, @NotNull Player clicker) {
        if (getClickHandler() != null) {
            getClickHandler().onClick(menuView, clicker);
        }
    }

}
