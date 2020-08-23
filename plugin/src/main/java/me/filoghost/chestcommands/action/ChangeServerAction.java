/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.action;

import me.filoghost.chestcommands.hook.BungeeCordHook;
import me.filoghost.chestcommands.placeholder.PlaceholderString;
import org.bukkit.entity.Player;

public class ChangeServerAction implements Action {

    private final PlaceholderString targetServer;
    
    public ChangeServerAction(String serializedAction) {
        targetServer = PlaceholderString.of(serializedAction);
    }

    @Override
    public void execute(Player player) {
        BungeeCordHook.connect(player, targetServer.getValue(player));
    }

}
