/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.chestcommands.listener;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.api.ClickResult;
import me.filoghost.chestcommands.inventory.DefaultItemInventory.SlotClickHandler;
import me.filoghost.chestcommands.menu.MenuManager;
import me.filoghost.chestcommands.inventory.DefaultItemInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.WeakHashMap;

public class InventoryListener implements Listener {

	private final MenuManager menuManager;
	private final Map<Player, Long> antiClickSpam;

	public InventoryListener(MenuManager menuManager) {
		this.menuManager = menuManager;
		this.antiClickSpam = new WeakHashMap<>();
	}
	

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onInteract(PlayerInteractEvent event) {
		if (event.hasItem() && event.getAction() != Action.PHYSICAL) {
			menuManager.openMenuByItem(event.getPlayer(), event.getItem(), event.getAction());
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onEarlyInventoryClick(InventoryClickEvent event) {
		if (MenuManager.isItemInventory(event.getInventory())) {
			// Cancel the event as early as possible
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onLateInventoryClick(InventoryClickEvent event) {
	    Inventory inventory = event.getInventory();
	    DefaultItemInventory itemInventory = MenuManager.getOpenItemInventory(inventory);
	    if (itemInventory == null) {
			return;
		}

	    // Cancel the event again just in case a plugin un-cancels it
	    event.setCancelled(true);

		int slot = event.getRawSlot();
		Player clicker = (Player) event.getWhoClicked();
		SlotClickHandler slotClickHandler = itemInventory.getSlotClickHandler(slot, clicker);
		if (slotClickHandler == null) {
			return;
		}

		Long cooldownUntil = antiClickSpam.get(clicker);
		long now = System.currentTimeMillis();
		int minDelay = ChestCommands.getSettings().anti_click_spam_delay;

		if (minDelay > 0) {
			if (cooldownUntil != null && cooldownUntil > now) {
				return;
			} else {
				antiClickSpam.put(clicker, now + minDelay);
			}
		}

		// Only handle the click AFTER the event has finished
		Bukkit.getScheduler().runTask(ChestCommands.getInstance(), () -> {
			ClickResult result = slotClickHandler.onClick();

			if (result == ClickResult.CLOSE) {
				clicker.closeInventory();
			}
		});
	}

}
