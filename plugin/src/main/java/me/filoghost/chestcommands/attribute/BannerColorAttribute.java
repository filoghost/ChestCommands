/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.parsing.ItemMetaParser;
import me.filoghost.chestcommands.parsing.ParseException;
import org.bukkit.DyeColor;

public class BannerColorAttribute implements IconAttribute {

    private final DyeColor dyeColor;

    public BannerColorAttribute(String serializedDyeColor, AttributeErrorHandler errorHandler) throws ParseException {
        this.dyeColor = ItemMetaParser.parseDyeColor(serializedDyeColor);
    }
    
    @Override
    public void apply(InternalConfigurableIcon icon) {
        icon.setBannerColor(dyeColor);
    }

}
