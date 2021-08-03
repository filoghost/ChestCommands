/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.menucreator;

import me.filoghost.chestcommands.api.Icon;
import me.filoghost.chestcommands.menu.InternalMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuCreatorPlayerInstance
{
    Player player;
    InternalMenu menu;
    ItemStack[] oldPlayerInventory;

    Inventory inventory;

    public MenuCreatorPlayerInstance(Player player, InternalMenu menu)
    {
        this.player = player;
        this.menu = menu;
        this.oldPlayerInventory = player.getInventory().getStorageContents();
    }

    public Player getPlayer()
    {
        return player;
    }

    public void restoreInventoryContents()
    {
        player.getInventory().setStorageContents(oldPlayerInventory);
    }

    public void openEditor()
    {
        inventory = Bukkit.createInventory(new MenuCreatorInventoryHolder(), menu.getRows() * 9, menu.getTitle());

        for(int i=0; i<menu.getIcons().getSize(); i++)
        {
            Icon tmp = menu.getIcons().getByIndex(i);
            if(tmp != null)
                inventory.setItem(i, tmp.render(player));
        }
        player.openInventory(inventory);

        player.getInventory().setStorageContents(new ItemStack[0]);

        player.getInventory().setItem(2, new ItemStack(Material.STONE));

    }

    public void reopenEditor()
    {
        if(inventory != null)
            player.openInventory(inventory);
    }
}
