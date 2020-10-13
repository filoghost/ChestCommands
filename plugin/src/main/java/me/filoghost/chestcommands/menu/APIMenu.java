/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.menu;

import me.filoghost.fcommons.Preconditions;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class APIMenu extends BaseMenu {

    private final Plugin owner;
    
    public APIMenu(@NotNull Plugin owner, @NotNull String title, int rows) {
        super(title, rows);
        Preconditions.notNull(owner, "owner");
        this.owner = owner;
    }

    public @NotNull Plugin getOwner() {
        return owner;
    }

}
