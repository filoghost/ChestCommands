/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.chestcommands.parsing.ParseException;

public class AmountAttribute implements IconAttribute {

    private final int amount;

    public AmountAttribute(int amount, AttributeErrorHandler errorHandler) throws ParseException {
        if (amount < 0) {
            throw new ParseException(Errors.Parsing.zeroOrPositive);
        }
        this.amount = amount;
    }
    
    @Override
    public void apply(InternalConfigurableIcon icon) {
        icon.setAmount(amount);
    }

}
