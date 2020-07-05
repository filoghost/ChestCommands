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

import me.filoghost.chestcommands.inventory.DefaultItemInventory;
import me.filoghost.chestcommands.menu.InternalIconMenu;
import me.filoghost.chestcommands.menu.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RefreshMenusTask implements Runnable {

	private long elapsedTenths;

	@Override
	public void run() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			DefaultItemInventory itemInventory = MenuManager.getOpenItemInventory(player);

			if (itemInventory == null || !(itemInventory.getMenu() instanceof InternalIconMenu)) {
				continue;
			}

			int refreshTicks = ((InternalIconMenu) itemInventory.getMenu()).getRefreshTicks();

			if (refreshTicks > 0 && elapsedTenths % refreshTicks == 0) {
				itemInventory.refresh();
			}
		}

		elapsedTenths++;
	}

}
