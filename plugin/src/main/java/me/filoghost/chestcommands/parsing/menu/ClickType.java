/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.parsing.menu;

import org.bukkit.event.block.Action;
import org.jetbrains.annotations.Nullable;

public enum ClickType {

    LEFT,
    RIGHT,
    BOTH;

    public static @Nullable ClickType fromOptions(boolean left, boolean right) {
        if (left && right) {
            return BOTH;
        } else if (left) {
            return LEFT;
        } else if (right) {
            return RIGHT;
        } else {
            return null;
        }
    }

    public boolean isValidInteract(Action action) {
        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            return this == LEFT || this == BOTH;
        } else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            return this == RIGHT || this == BOTH;
        } else {
            return false;
        }
    }

}
