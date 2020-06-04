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
package me.filoghost.chestcommands.task;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import me.filoghost.chestcommands.internal.ExtendedIconMenu;
import me.filoghost.chestcommands.internal.MenuInventoryHolder;
import me.filoghost.chestcommands.util.BukkitUtils;

public class RefreshMenusTask implements Runnable {

	private long elapsedTenths;

	@Override
	public void run() {

		for (Player player : BukkitUtils.getOnlinePlayers()) {

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
