/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.hook;

import me.confuser.barapi.BarAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public enum BarAPIHook implements PluginHook {

    INSTANCE;

    private boolean enabled;

    @Override
    public void setup() {
        enabled = Bukkit.getPluginManager().getPlugin("BarAPI") != null;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @SuppressWarnings("deprecation")
    public static void setMessage(Player player, String message, int seconds) {
        INSTANCE.checkEnabledState();

        BarAPI.setMessage(player, message, seconds);
    }

}
