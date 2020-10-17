/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.api;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Callback to provide a placeholder replacement.
 *
 * @since 1
 */
@FunctionalInterface
public interface PlaceholderReplacer {

    /**
     * Callback for providing a placeholder replacement, given a player and the argument of the placeholder (if
     * present).
     * <p>
     * For example, the placeholder {test} has a null argument and the placeholder {test: hello world} has the string
     * argument "hello world".
     * <p>
     * If this method returns null, the placeholder is not replaced. It is preferred to return a descriptive error
     * message rather than returning null.
     * <p>
     * <b>Warning</b>: this method should be performance efficient, as it may be invoked quite often.
     *
     * @param player   the player viewing the placeholder
     * @param argument the argument inside the placeholder, if present
     * @return the placeholder replacement, null to not replace
     * @see ChestCommandsAPI#registerPlaceholder(Plugin, String, PlaceholderReplacer)
     * @since 1
     */
    @Nullable String getReplacement(@NotNull Player player, @Nullable String argument);

}
