/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;

public class SkullOwnerAttribute implements IconAttribute {

    private final String skullOwner;

    public SkullOwnerAttribute(String skullOwner, AttributeErrorHandler errorHandler) {
        this.skullOwner = skullOwner;
    }
    
    @Override
    public void apply(InternalConfigurableIcon icon) {
        icon.setSkullOwner(skullOwner);
    }

}
