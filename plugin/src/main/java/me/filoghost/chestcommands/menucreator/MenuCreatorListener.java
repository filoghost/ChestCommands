/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.menucreator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class MenuCreatorListener implements Listener
{
    private static MenuCreatorListener instance;

    Plugin plugin;

    public MenuCreatorListener(Plugin plugin)
    {
        instance = this;
        this.plugin = plugin;
    }

    public static MenuCreatorListener getInstance()
    {
        return instance;
    }

    public void registerEvents()
    {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }



    public void openGuiEditor(Player sender)
    {


    }
}
