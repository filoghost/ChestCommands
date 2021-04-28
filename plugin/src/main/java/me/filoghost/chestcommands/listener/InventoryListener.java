/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.listener;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.api.Icon;
import me.filoghost.chestcommands.api.Menu;
import me.filoghost.chestcommands.config.Settings;
import me.filoghost.chestcommands.inventory.DefaultMenuView;
import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.chestcommands.menu.InternalMenu;
import me.filoghost.chestcommands.menu.MenuManager;
import me.filoghost.fcommons.logging.Log;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.WeakHashMap;

public class InventoryListener implements Listener {

    private final Map<Player, Long> antiClickSpam = new WeakHashMap<>();


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onInteract(PlayerInteractEvent event) {
        if (event.hasItem() && event.getAction() != Action.PHYSICAL) {
            MenuManager.openMenuByItem(event.getPlayer(), event.getItem(), event.getAction());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onEarlyInventoryClick(InventoryClickEvent event) {
        if (MenuManager.isMenuInventory(event.getInventory())) {
            // Cancel the event as early as possible
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onLateInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        DefaultMenuView menuView = MenuManager.getOpenMenuView(inventory);
        if (menuView == null) {
            return;
        }

        // Cancel the event again just in case a plugin un-cancels it
        event.setCancelled(true);

        int slot = event.getRawSlot();
        Player clicker = (Player) event.getWhoClicked();
        Icon icon = menuView.getIcon(slot);
        if (icon == null) {
            return;
        }

        Long cooldownUntil = antiClickSpam.get(clicker);
        long now = System.currentTimeMillis();
        int minDelay = Settings.get().anti_click_spam_delay;

        if (minDelay > 0) {
            if (cooldownUntil != null && cooldownUntil > now) {
                return;
            } else {
                antiClickSpam.put(clicker, now + minDelay);
            }
        }

        // Only handle the click AFTER the event has finished
        Bukkit.getScheduler().runTask(ChestCommands.getInstance(), () -> {
            try {
                icon.onClick(menuView, clicker);
            } catch (Throwable t) {
                handleIconClickException(clicker, menuView.getMenu(), t);
                menuView.close();
            }
        });
    }

    private void handleIconClickException(Player clicker, Menu menu, Throwable throwable) {
        String menuDescription;
        if (menu.getPlugin() == ChestCommands.getInstance()) {
            menuDescription = "the menu \"" + Errors.formatPath(((InternalMenu) menu).getSourceFile()) + "\"";
        } else {
            menuDescription = "a menu created by the plugin \"" + menu.getPlugin().getName() + "\"";
        }

        Log.severe("Encountered an exception while handling a click inside " + menuDescription, throwable);
        clicker.sendMessage(ChatColor.RED + "An internal error occurred when you clicked on the item.");
    }

}
