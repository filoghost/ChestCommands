/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.action;

import me.filoghost.chestcommands.placeholder.PlaceholderString;
import org.bukkit.entity.Player;

public class PlayerCommandAction implements Action {

    private final PlaceholderString command;

    public PlayerCommandAction(String serializedAction) {
        command = PlaceholderString.of(serializedAction);
    }

    @Override
    public void execute(Player player) {
        player.chat('/' + command.getValue(player));
    }

}
