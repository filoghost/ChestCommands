/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.test;

import me.filoghost.chestcommands.DefaultBackendAPI;
import me.filoghost.chestcommands.api.internal.BackendAPI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static org.mockito.Mockito.*;

public final class BukkitMocks {

    public static final Plugin PLUGIN;
    public static final Player PLAYER;

    static {
        // Server server = mock(Server.class, RETURNS_DEEP_STUBS);
        // Bukkit.setServer(server);
        PLUGIN = mock(Plugin.class);
        when(PLUGIN.getName()).thenReturn("MockPlugin");
        PLAYER = mock(Player.class);
        when(PLAYER.getName()).thenReturn("filoghost");
        BackendAPI.setImplementation(new DefaultBackendAPI());
    }

}