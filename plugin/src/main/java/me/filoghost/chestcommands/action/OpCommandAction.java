/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.action;

import me.filoghost.chestcommands.placeholder.PlaceholderString;
import org.bukkit.entity.Player;

public class OpCommandAction implements Action {

    private final PlaceholderString command;

    public OpCommandAction(String serializedAction) {
        command = PlaceholderString.of(serializedAction);
    }

    @Override
    public void execute(Player player) {
        if (player.isOp()) {
            player.chat("/" + command.getValue(player));
        } else {
            player.setOp(true);
            player.chat("/" + command.getValue(player));
            player.setOp(false);
        }
    }

}
