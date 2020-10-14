/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import dev.lone.itemsadder.api.ItemsAdder;
import me.filoghost.chestcommands.hook.ItemsAdderHook;
import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.parsing.ParseException;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItemAttribute implements IconAttribute {
    ItemStack item;

    public CustomItemAttribute(String customItemName, AttributeErrorHandler errorHandler) throws ParseException
    {
        if(ItemsAdderHook.INSTANCE.isEnabled())
        {
            if(ItemsAdder.isCustomItem(customItemName))
            {
                this.item = ItemsAdder.getCustomItem(customItemName);
                return;
            }
        }
        //....other plugins
    }

    @Override
    public void apply(InternalConfigurableIcon icon) {
        if(item != null)
        {
            icon.setMaterial(item.getType());
            ItemMeta meta = item.getItemMeta();
            icon.setCustomModelData(meta.getCustomModelData());
            if(icon.getLore() == null)
                icon.setLore(meta.getLore());
            if(icon.getName() == null)
                icon.setName(meta.getDisplayName());
        }
    }

}
