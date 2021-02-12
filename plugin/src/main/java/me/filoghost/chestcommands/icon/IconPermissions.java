/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.icon;

import me.filoghost.chestcommands.config.Lang;
import me.filoghost.chestcommands.placeholder.PlaceholderString;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class IconPermissions {

    private List<IconPermission> permissions;

    public IconPermissions(List<String> permissions) {
        if (permissions != null && permissions.size() > 0) {
            this.permissions = new ArrayList<>();
            for (String perm : permissions) {
                this.permissions.add(new IconPermission(perm));
            }
        }
    }

    public boolean checkAndSendMessages(Player player, List<PlaceholderString> messages) {
        if (isEmpty()) {
            return true;
        }

        boolean result = true;
        int messagesiz = messages != null ? messages.size() : 0;

        for (int i = 0; i < permissions.size(); i++) {
            IconPermission perm = permissions.get(i);
            if (!IconPermission.hasPermission(player, perm)) {
                result = false;
                if (messagesiz >= i + 1) {
                    PlaceholderString pstring = messages.get(i);
                    if (pstring != null) {
                        player.sendMessage(pstring.getValue(player));
                    } else {
                        player.sendMessage(Lang.default_no_icon_permission);
                    }
                }
            }
        }
        return result;
    }

    public boolean isEmpty() {
        return this.permissions == null || this.permissions.isEmpty();
    }

}
