/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.menucreator;

import me.filoghost.chestcommands.menu.InternalMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;

import java.util.WeakHashMap;

public class MenuCreatorListener implements Listener
{
    private static MenuCreatorListener instance;

    Plugin plugin;
    WeakHashMap<Player, MenuCreatorPlayerInstance> currentlyEditing = new WeakHashMap<>();

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

    public void openGuiEditor(Player player, InternalMenu menu)
    {
        MenuCreatorPlayerInstance menuCreatorPlayerInstance = new MenuCreatorPlayerInstance(player, menu);
        menuCreatorPlayerInstance.openEditor();
        currentlyEditing.put(player, menuCreatorPlayerInstance);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private void avoidClosingInventory(InventoryCloseEvent e)
    {
        if(!(e.getInventory().getHolder() instanceof MenuCreatorInventoryHolder))
            return;

        MenuCreatorPlayerInstance menuCreatorPlayerInstance = currentlyEditing.get(e.getPlayer());
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            menuCreatorPlayerInstance.reopenEditor();
        }, 1L);
    }
}
