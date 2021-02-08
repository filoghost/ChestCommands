package me.filoghost.chestcommands.icon.requirement;

import org.bukkit.entity.Player;

public class RequiredPoints implements Requirement {
    @Override
    public boolean hasCost(Player player) {
        return false;
    }

    @Override
    public boolean takeCost(Player player) {
        return false;
    }
}
