package com.gmail.filoghost.chestcommands.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {
	
	public static boolean hasInventoryFull(Player player) {
		return player.getInventory().firstEmpty() == -1;
	}
	
	public static boolean containsAtLeast(Inventory inv, Material material, int minAmount) {
		int contained = 0;
		
		for (ItemStack item : inv.getContents()) {
			if (item != null && item.getType() == material) {
				contained += item.getAmount();
			}
		}
		
		return contained >= minAmount;
	}

	public static boolean containsAtLeast(Inventory inv, Material material, int minAmount, short data) {
		int contained = 0;
		
		for (ItemStack item : inv.getContents()) {
			if (item != null && item.getType() == material && item.getDurability() == data) {
				contained += item.getAmount();
			}
		}
		
		return contained >= minAmount;
	}
	
}
