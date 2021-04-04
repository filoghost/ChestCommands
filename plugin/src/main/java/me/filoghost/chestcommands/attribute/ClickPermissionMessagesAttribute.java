/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;

import java.util.List;

public class ClickPermissionMessagesAttribute implements IconAttribute {

    private final List<String> messages;

    public ClickPermissionMessagesAttribute(List<String> messages, AttributeErrorHandler errorHandler) {
        this.messages = messages;
    }

    @Override
    public void apply(InternalConfigurableIcon icon) {
        icon.setNoclickPermissionMessages(this.messages);
    }

}
