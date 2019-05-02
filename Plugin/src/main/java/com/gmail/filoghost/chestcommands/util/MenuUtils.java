package com.gmail.filoghost.chestcommands.util;

import com.gmail.filoghost.chestcommands.internal.ExtendedIconMenu;
import com.gmail.filoghost.chestcommands.internal.MenuInventoryHolder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public final class MenuUtils {

	private MenuUtils() {
	}

	public static void refreshMenu(Player player) {
		InventoryView view = player.getOpenInventory();
		if (view != null) {
			Inventory topInventory = view.getTopInventory();
			if (topInventory.getHolder() instanceof MenuInventoryHolder) {
				MenuInventoryHolder menuHolder = (MenuInventoryHolder) topInventory.getHolder();

				if (menuHolder.getIconMenu() instanceof ExtendedIconMenu) {
					((ExtendedIconMenu) menuHolder.getIconMenu()).refresh(player, topInventory);
				}
			}
		}
	}
}
