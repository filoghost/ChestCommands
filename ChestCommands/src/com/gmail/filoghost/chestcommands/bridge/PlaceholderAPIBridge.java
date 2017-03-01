package com.gmail.filoghost.chestcommands.bridge;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.clip.placeholderapi.PlaceholderAPI;

public class PlaceholderAPIBridge {

	public static List<String> replace(Player p, List<String> content) {
		return PlaceholderAPI.setPlaceholders(p, content);
	}

	public static ItemStack replace(Player p, ItemStack item) {
		if (item == null) {
			return null;
		}
		if (!item.hasItemMeta()) {
			return item;
		}
		ItemStack newItem = item.clone();
		ItemMeta m = newItem.getItemMeta();
		if (newItem.getItemMeta().hasDisplayName()) {
			m.setDisplayName(PlaceholderAPI.setPlaceholders(p, newItem.getItemMeta().getDisplayName()));
		}
		if (newItem.getItemMeta().hasLore()) {
			m.setLore(PlaceholderAPI.setPlaceholders(p, newItem.getItemMeta().getLore()));
		}
		newItem.setItemMeta(m);
		return newItem;
	}
}
