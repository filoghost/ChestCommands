/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.menu;

import me.filoghost.fcommons.Preconditions;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class APIMenu extends BaseMenu {

    private final Plugin plugin;

    public APIMenu(@NotNull Plugin plugin, @NotNull String title, int rows) {
        super(title, rows);
        Preconditions.notNull(plugin, "plugin");
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

}
