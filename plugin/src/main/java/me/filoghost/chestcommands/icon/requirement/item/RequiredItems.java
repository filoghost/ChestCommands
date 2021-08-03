/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.icon.requirement.item;

import com.google.common.collect.ImmutableList;
import me.filoghost.chestcommands.config.Lang;
import me.filoghost.chestcommands.icon.requirement.Requirement;
import me.filoghost.chestcommands.util.Utils;
import org.bukkit.entity.Player;

import java.util.List;

public class RequiredItems implements Requirement {

    private final List<RequiredItem> items;
    
    public RequiredItems(List<RequiredItem> items) {
        this.items = ImmutableList.copyOf(items);
    }

    @Override
    public boolean hasCost(Player player) {
        InventoryTakeHelper inventoryTakeHelper = new InventoryTakeHelper(player.getInventory());
        boolean hasItems = inventoryTakeHelper.prepareTakeItems(items);

        if (!hasItems) {
            for (RequiredItem item : items) {
                player.sendMessage(Lang.get().no_required_item
                        .replace("{material}", Utils.formatEnum(item.getMaterial()))
                        .replace("{amount}", Integer.toString(item.getAmount()))
                        .replace("{durability}", item.hasRestrictiveDurability() ? Short.toString(item.getDurability()) : Lang.get().any));
            }
        }
        
        return hasItems;
    }

    @Override
    public boolean takeCost(Player player) {
        InventoryTakeHelper inventoryTakeHelper = new InventoryTakeHelper(player.getInventory());
        boolean hasItems = inventoryTakeHelper.prepareTakeItems(items);

        if (!hasItems) {
            return false;
        }
        
        inventoryTakeHelper.applyTakeItems();
        return true;
    }
    
}
