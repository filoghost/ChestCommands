/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;

public class DurabilityAttribute implements IconAttribute {

    private final short durability;

    public DurabilityAttribute(short durability, AttributeErrorHandler errorHandler) {
        this.durability = durability;
    }
    
    @Override
    public void apply(InternalConfigurableIcon icon) {
        icon.setDurability(durability);
    }

}
