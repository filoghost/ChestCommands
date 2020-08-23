/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.chestcommands.parsing.ParseException;

public class ExpLevelsAttribute implements IconAttribute {

    private final int expLevels;

    public ExpLevelsAttribute(int expLevels, AttributeErrorHandler errorHandler) throws ParseException {
        if (expLevels < 0) {
            throw new ParseException(Errors.Parsing.zeroOrPositive);
        }
        this.expLevels = expLevels;
    }
    
    @Override
    public void apply(InternalConfigurableIcon icon) {
        icon.setRequiredExpLevel(expLevels);
    }

}
