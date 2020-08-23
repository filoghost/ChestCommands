/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.parsing.ParseException;

public interface AttributeErrorHandler {

    void onListElementError(String listElement, ParseException e);

}
