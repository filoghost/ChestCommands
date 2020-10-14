/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.hook;

import dev.lone.itemsadder.api.Events.ItemsAdderFirstLoadEvent;
import dev.lone.itemsadder.api.ItemsAdder;
import me.filoghost.chestcommands.ChestCommands;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public enum ItemsAdderHook implements PluginHook, Listener
{

    INSTANCE;

    private boolean enabled;

    @Override
    public void setup() {
        enabled = Bukkit.getPluginManager().getPlugin("ItemsAdder") != null;
        if(ItemsAdder.areItemsLoaded())
            ChestCommands.loadAll();
        else
            Bukkit.getPluginManager().registerEvents(this, ChestCommands.getPluginInstance());
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onItemsLoaded(ItemsAdderFirstLoadEvent e)
    {
        ChestCommands.loadAll();
    }
}
