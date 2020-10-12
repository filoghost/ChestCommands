/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.hook;

import org.bukkit.Bukkit;

public enum ItemsAdderHook implements PluginHook {

    INSTANCE;

    private boolean enabled;

    @Override
    public void setup() {
        enabled = Bukkit.getPluginManager().getPlugin("ItemsAdder") != null;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
