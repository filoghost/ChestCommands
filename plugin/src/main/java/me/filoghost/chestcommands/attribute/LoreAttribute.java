/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.config.Settings;
import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.fcommons.Colors;
import me.filoghost.fcommons.collection.CollectionUtils;

import java.util.List;

public class LoreAttribute implements IconAttribute {

    private final List<String> lore;
    
    public LoreAttribute(List<String> lore, AttributeErrorHandler errorHandler) {
        this.lore = colorLore(lore);
    }

    private List<String> colorLore(List<String> input) {
        return CollectionUtils.toArrayList(input, line -> {
            if (!line.isEmpty()) {
                return Settings.get().default_color__lore + Colors.addColors(line);
            } else {
                return line;
            }
        });
    }

    @Override
    public void apply(InternalConfigurableIcon icon) {
        icon.setLore(lore);
    }

}
