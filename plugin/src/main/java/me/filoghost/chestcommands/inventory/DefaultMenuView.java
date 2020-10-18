/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.inventory;

import me.filoghost.chestcommands.api.Icon;
import me.filoghost.chestcommands.api.MenuView;
import me.filoghost.chestcommands.icon.RefreshableIcon;
import me.filoghost.chestcommands.menu.BaseMenu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultMenuView implements MenuView {

    private final BaseMenu menu;
    private final Player viewer;
    private final InventoryGrid bukkitInventory;

    public DefaultMenuView(@NotNull BaseMenu menu, @NotNull Player viewer) {
        this.menu = menu;
        this.viewer = viewer;
        this.bukkitInventory = new InventoryGrid(new MenuInventoryHolder(this), menu.getRows(), menu.getTitle());
        refresh();
    }

    @Override
    public void refresh() {
        for (int i = 0; i < menu.getIcons().getSize(); i++) {
            Icon icon = menu.getIcons().getByIndex(i);

            if (icon == null) {
                bukkitInventory.setByIndex(i, null);
            } else if (icon instanceof RefreshableIcon) {
                ItemStack newItemStack = ((RefreshableIcon) icon).updateRendering(viewer, bukkitInventory.getByIndex(i));
                bukkitInventory.setByIndex(i, newItemStack);
            } else {
                bukkitInventory.setByIndex(i, icon.render(viewer));
            }
        }
    }

    @Override
    public void close() {
        if (viewer.isOnline()) {
            viewer.closeInventory();
        }
    }

    public void open() {
        viewer.openInventory(bukkitInventory.getInventory());
    }

    public @Nullable Icon getIcon(int slot) {
        if (slot < 0 || slot >= bukkitInventory.getSize()) {
            return null;
        }

        return menu.getIcons().getByIndex(slot);
    }

    @Override
    public @NotNull BaseMenu getMenu() {
        return menu;
    }

    @Override
    public @NotNull Player getViewer() {
        return viewer;
    }

}
