/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.chestcommands.parsing.ParseException;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomModelDataAttribute implements IconAttribute {

    private final int customModelData;

    public CustomModelDataAttribute(int customModelData, AttributeErrorHandler errorHandler) throws ParseException {
        if (customModelData < 0) {
            throw new ParseException(Errors.Parsing.zeroOrPositive);
        }
        this.customModelData = customModelData;
    }
    
    @Override
    public void apply(InternalConfigurableIcon icon)
    {
        icon.setCustomModelData(customModelData);
    }

}
