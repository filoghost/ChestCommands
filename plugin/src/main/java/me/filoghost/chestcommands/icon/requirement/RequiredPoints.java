/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.icon.requirement;

import com.google.common.base.Preconditions;
import me.filoghost.chestcommands.config.Lang;
import me.filoghost.chestcommands.hook.PlayerPointsHook;
import me.filoghost.chestcommands.logging.Errors;
import org.bukkit.entity.Player;

public class RequiredPoints implements Requirement {

    private final int points;

    public RequiredPoints(int points) {
        Preconditions.checkArgument(points > 0, "points must be positive");
        this.points = points;
    }

    @Override
    public boolean hasCost(Player player) {
        if (!PlayerPointsHook.INSTANCE.isEnabled()) {
            player.sendMessage(Errors.User.configurationError(
                    "This item requires player points, but PlayerPoints plugin was not found. "
                            + "For security, the action has been blocked"));
            return false;
        }

        if (PlayerPointsHook.INSTANCE.look(player.getUniqueId()) < points) {
            player.sendMessage(Lang.no_points.replace("{points}", String.valueOf(points)));
            return false;
        }

        return true;
    }

    @Override
    public boolean takeCost(Player player) {
        boolean success = PlayerPointsHook.INSTANCE.take(player.getUniqueId(), points);

        if (!success) {
            player.sendMessage(Errors.User.configurationError("a points transaction couldn't be executed"));
        }

        return success;
    }
}
