/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.action;

import me.filoghost.chestcommands.listener.InventoryListener;
import me.filoghost.chestcommands.placeholder.PlaceholderString;
import org.bukkit.entity.Player;

public class CloseThisMenuAction implements Action {

    private final PlaceholderString targetMenu;

    public CloseThisMenuAction(String serializedAction) {
        targetMenu = PlaceholderString.of(serializedAction);
    }

    @Override
    public void execute(final Player player) {
        InventoryListener.setCanPlayerClose_AutoOpenMenu(player, true);
        player.closeInventory();
    }

}
