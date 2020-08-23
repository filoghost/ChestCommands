/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.icon.requirement;

import org.bukkit.entity.Player;

public interface Requirement {
    
    boolean hasCost(Player player);
    
    boolean takeCost(Player player);
    
    static boolean hasAllCosts(Player player, Requirement... requirements) {
        for (Requirement requirement : requirements) {
            if (requirement != null && !requirement.hasCost(player)) {
                return false;
            }
        }
        
        return true;
    }
    
    static boolean takeAllCosts(Player player, Requirement... requirements) {
        for (Requirement requirement : requirements) {
            if (requirement != null) {
                boolean success = requirement.takeCost(player);
                if (!success) {
                    return false;
                }
            }
        }
        
        return true;
    }

}
