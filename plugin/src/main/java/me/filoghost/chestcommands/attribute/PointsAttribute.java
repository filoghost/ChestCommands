/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.chestcommands.parsing.ParseException;

public class PointsAttribute implements IconAttribute{

    private final int points;

    public PointsAttribute(int points, AttributeErrorHandler errorHandler) throws ParseException {
        if (points < 0) {
            throw new ParseException(Errors.Parsing.zeroOrPositive);
        }
        this.points = points;
    }

    @Override
    public void apply(InternalConfigurableIcon icon) {
        icon.setRequiredPoints(points);
    }

}
