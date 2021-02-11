/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.hook;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public enum PlayerPointsHook implements PluginHook {

    INSTANCE;

    private PlayerPointsAPI pointsApi;

    @Override
    public void setup() {
        pointsApi = null;
        Plugin ppPlugin = Bukkit.getPluginManager().getPlugin("PlayerPoints");
        if (ppPlugin != null && ppPlugin.isEnabled()) {
            pointsApi = ((PlayerPoints)ppPlugin).getAPI();
        }
    }

    @Override
    public boolean isEnabled() {
        return pointsApi != null;
    }

    public boolean give(UUID playerId, int amount) {
        checkEnabledState();
        return pointsApi.give(playerId, amount);
    }

    public boolean take(UUID playerId, int amount) {
        checkEnabledState();
        return pointsApi.take(playerId, amount);
    }

    public int look(UUID playerId) {
        checkEnabledState();
        return pointsApi.look(playerId);
    }

}
