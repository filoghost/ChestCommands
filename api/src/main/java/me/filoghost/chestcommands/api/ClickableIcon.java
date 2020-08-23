/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.api;

import org.bukkit.entity.Player;

public interface ClickableIcon extends Icon {

    void setClickHandler(ClickHandler clickHandler);

    ClickHandler getClickHandler();

    @Override
    default ClickResult onClick(MenuView menuView, Player clicker) {
        if (getClickHandler() != null) {
            return getClickHandler().onClick(menuView, clicker);
        } else {
            return ClickResult.KEEP_OPEN;
        }
    }

}
