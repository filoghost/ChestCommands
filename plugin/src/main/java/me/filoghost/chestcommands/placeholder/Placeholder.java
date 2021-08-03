/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.placeholder;

import me.filoghost.chestcommands.api.PlaceholderReplacer;
import org.bukkit.plugin.Plugin;

public class Placeholder {

    private final Plugin plugin;
    private final PlaceholderReplacer placeholderReplacer;

    public Placeholder(Plugin plugin, PlaceholderReplacer placeholderReplacer) {
        this.plugin = plugin;
        this.placeholderReplacer = placeholderReplacer;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public PlaceholderReplacer getReplacer() {
        return placeholderReplacer;
    }

}
