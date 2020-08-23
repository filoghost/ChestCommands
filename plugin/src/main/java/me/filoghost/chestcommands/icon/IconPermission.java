/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.icon;

import me.filoghost.fcommons.Strings;
import org.bukkit.entity.Player;

public class IconPermission {
    
    private final String permission;
    private final boolean negated;

    public IconPermission(String permission) {
        if (permission != null) {
            permission = permission.trim();
        }
        
        if (Strings.isEmpty(permission)) {
            this.permission = null;
            negated = false;
        } else {
            if (permission.startsWith("-")) {
                this.permission = permission.substring(1);
                negated = true;
            } else {
                this.permission = permission;
                negated = false;
            }
        }
    }
    
    private boolean hasPermission(Player player) {
        if (isEmpty()) {
            return true;
        }

        if (negated) {
            return !player.hasPermission(permission);
        } else {
            return player.hasPermission(permission);
        }
    }

    public boolean isEmpty() {
        return this.permission == null;
    }

    public static boolean hasPermission(Player player, IconPermission permission) {
        return permission == null || permission.hasPermission(player);
    }

}
