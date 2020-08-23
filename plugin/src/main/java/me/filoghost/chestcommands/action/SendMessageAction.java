/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.action;

import me.filoghost.chestcommands.placeholder.PlaceholderString;
import me.filoghost.fcommons.Colors;
import org.bukkit.entity.Player;

public class SendMessageAction implements Action {
    
    private final PlaceholderString message;

    public SendMessageAction(String serializedAction) {
        message = PlaceholderString.of(Colors.addColors(serializedAction));
    }

    @Override
    public void execute(Player player) {
        player.sendMessage(message.getValue(player));
    }

}
