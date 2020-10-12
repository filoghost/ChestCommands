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

public class CustomModelDataAutomaticAttribute implements IconAttribute {
    private final int customModelData;

    public CustomModelDataAutomaticAttribute(String customItemName, AttributeErrorHandler errorHandler) throws ParseException
    {
        if(ItemsAdderHook.INSTANCE.isEnabled())
        {
            if(ItemsAdder.isCustomItem(customItemName))
            {
                ItemStack tmp = ItemsAdder.getCustomItem(customItemName);
                this.customModelData = tmp.getItemMeta().getCustomModelData();
                return;
            }
        }
        this.customModelData = 0;
    }

    @Override
    public void apply(InternalConfigurableIcon icon) {
        if(customModelData != 0)
            icon.setNBTData("{CustomModelData:" + customModelData + "}");
    }

}
