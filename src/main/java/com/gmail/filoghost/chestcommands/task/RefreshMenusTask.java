package com.gmail.filoghost.chestcommands.task;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import com.gmail.filoghost.chestcommands.internal.ExtendedIconMenu;
import com.gmail.filoghost.chestcommands.internal.MenuInventoryHolder;
import com.gmail.filoghost.chestcommands.util.VersionUtils;

public class RefreshMenusTask implements Runnable {
	
	private long elapsedTenths;

	@Override
	public void run() {

		for (Player player : VersionUtils.getOnlinePlayers()) {
			
			InventoryView view = player.getOpenInventory();
			if (view == null) {
				return;
			}
			
			Inventory topInventory = view.getTopInventory();
			if (topInventory.getHolder() instanceof MenuInventoryHolder) {
				MenuInventoryHolder menuHolder = (MenuInventoryHolder) topInventory.getHolder();
				
				if (menuHolder.getIconMenu() instanceof ExtendedIconMenu) {
					ExtendedIconMenu extMenu = (ExtendedIconMenu) menuHolder.getIconMenu();
					
					if (extMenu.getRefreshTicks() > 0) {
						if (elapsedTenths % extMenu.getRefreshTicks() == 0) {
							extMenu.refresh(player, topInventory);
						}
					}
				}
			}
		}
		
		elapsedTenths++;
	}

}
