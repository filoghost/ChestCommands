/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.action;

import me.filoghost.chestcommands.parsing.ItemStackParser;
import me.filoghost.chestcommands.parsing.ParseException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveItemAction implements Action {

    private final ItemStack itemToGive;

    public GiveItemAction(String serializedAction) throws ParseException {
        ItemStackParser reader = new ItemStackParser(serializedAction, true);
        reader.checkNotAir();
        itemToGive = reader.createStack();
    }

    @Override
    public void execute(Player player) {
        player.getInventory().addItem(itemToGive.clone());
    }

}
