/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.parsing.ParseException;

public class BlankAttribute implements IconAttribute {
    boolean isBlank;

    public BlankAttribute(boolean isBlank, AttributeErrorHandler errorHandler) throws ParseException
    {
        this.isBlank = isBlank;
    }

    @Override
    public void apply(InternalConfigurableIcon icon) {
        if(isBlank)
        {
            icon.setBlank(true);
        }
    }

}
