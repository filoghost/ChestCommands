/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.inventory;

import me.filoghost.fcommons.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MenuInventoryHolder implements InventoryHolder {

    private final DefaultMenuView menuView;

    public MenuInventoryHolder(DefaultMenuView menuView) {
        Preconditions.notNull(menuView, "menuView");
        this.menuView = menuView;
    }

    @Override
    public Inventory getInventory() {
        /*
         * This inventory will not do anything.
         * I'm 90% sure that it doesn't break any other plugin,
         * because the only way you can get here is using InventoryClickEvent,
         * that is cancelled by ChestCommands, or using InventoryOpenEvent.
         */
        return Bukkit.createInventory(null, 9);
    }

    public DefaultMenuView getMenuView() {
        return menuView;
    }
}
