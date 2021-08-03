/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.icon.requirement;

import com.google.common.base.Preconditions;
import me.filoghost.chestcommands.config.Lang;
import org.bukkit.entity.Player;

public class RequiredExpLevel implements Requirement {

    private final int levels;

    public RequiredExpLevel(int levels) {
        Preconditions.checkArgument(levels > 0, "levels must be positive");
        this.levels = levels;
    }

    @Override
    public boolean hasCost(Player player) {
        if (player.getLevel() < levels) {
            player.sendMessage(Lang.get().no_exp.replace("{levels}", Integer.toString(levels)));
            return false;
        }

        return true;
    }

    @Override
    public boolean takeCost(Player player) {
        int newLevel = player.getLevel() - levels;
        if (newLevel < 0) {
            newLevel = 0;
        }

        player.setLevel(newLevel);
        return true;
    }

}
