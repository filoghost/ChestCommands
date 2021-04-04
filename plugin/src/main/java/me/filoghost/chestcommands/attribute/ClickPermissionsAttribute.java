/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.icon.IconPermissions;
import me.filoghost.chestcommands.icon.InternalConfigurableIcon;

import java.util.List;

public class ClickPermissionsAttribute implements IconAttribute{

    private final IconPermissions permissions;

    public ClickPermissionsAttribute(List<String> permissions, AttributeErrorHandler errorHandler){
        this.permissions = new IconPermissions(permissions);
    }

    @Override
    public void apply(InternalConfigurableIcon icon) {
        icon.setClickPermissions(this.permissions);
    }
}
