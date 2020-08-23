/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.api;

import me.filoghost.chestcommands.api.internal.BackendAPI;
import org.bukkit.inventory.ItemStack;

public interface StaticIcon extends ClickableIcon {
    
    static StaticIcon create(ItemStack itemStack) {
        return BackendAPI.getImplementation().createStaticIcon(itemStack);
    }

    ItemStack getItemStack();

    void setItemStack(ItemStack itemStack);

}
