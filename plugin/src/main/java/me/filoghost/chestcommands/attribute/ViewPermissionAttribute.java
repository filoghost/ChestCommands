/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;

public class ViewPermissionAttribute implements IconAttribute {

    private final String viewPermission;

    public ViewPermissionAttribute(String viewPermission, AttributeErrorHandler errorHandler) {
        this.viewPermission = viewPermission;
    }

    @Override
    public void apply(InternalConfigurableIcon icon) {
        icon.setViewPermission(viewPermission);
    }

}
