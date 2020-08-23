/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.parsing.ItemMetaParser;
import me.filoghost.chestcommands.parsing.ParseException;
import org.bukkit.Color;

public class LeatherColorAttribute implements IconAttribute {

    private final Color color;

    public LeatherColorAttribute(String serializedColor, AttributeErrorHandler errorHandler) throws ParseException {
        this.color = ItemMetaParser.parseRGBColor(serializedColor);
    }

    @Override
    public void apply(InternalConfigurableIcon icon) {
        icon.setLeatherColor(color);
    }

}
