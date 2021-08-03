/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.listener;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.Permissions;
import me.filoghost.chestcommands.config.Settings;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (ChestCommands.getLastLoadErrors().hasErrors() && player.hasPermission(Permissions.SEE_ERRORS)) {
            player.sendMessage(
                    ChestCommands.CHAT_PREFIX + ChatColor.RED + "The plugin found " + ChestCommands.getLastLoadErrors().getErrorsCount()
                    + " error(s) last time it was loaded. You can see them by doing \"/cc reload\" in the console.");
        }

        if (ChestCommands.hasNewVersion() && Settings.get().update_notifications && player.hasPermission(Permissions.UPDATE_NOTIFICATIONS)) {
            player.sendMessage(ChestCommands.CHAT_PREFIX + "Found an update: " + ChestCommands.getNewVersion() + ". Download:");
            player.sendMessage(ChatColor.DARK_GREEN + ">> " + ChatColor.GREEN + "http://dev.bukkit.org/bukkit-plugins/chest-commands");
        }
    }

}
