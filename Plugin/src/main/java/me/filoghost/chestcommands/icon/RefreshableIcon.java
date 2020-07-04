package me.filoghost.chestcommands.icon;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface RefreshableIcon {
	
	ItemStack updateRendering(Player viewer, ItemStack currentRendering);
	
}
