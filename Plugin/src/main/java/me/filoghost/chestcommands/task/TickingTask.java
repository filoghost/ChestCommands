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

import me.filoghost.chestcommands.inventory.DefaultMenuInventory;
import me.filoghost.chestcommands.menu.InternalIconMenu;
import me.filoghost.chestcommands.menu.MenuManager;
import me.filoghost.chestcommands.placeholder.PlaceholderManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TickingTask implements Runnable {

	private long currentTick;

	@Override
	public void run() {
		updateInventories();
		PlaceholderManager.onTick();

		currentTick++;
	}

	private void updateInventories() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			DefaultMenuInventory menuInventory = MenuManager.getOpenMenuInventory(player);

			if (menuInventory == null || !(menuInventory.getIconMenu() instanceof InternalIconMenu)) {
				continue;
			}

			int refreshTicks = ((InternalIconMenu) menuInventory.getIconMenu()).getRefreshTicks();

			if (refreshTicks > 0 && currentTick % refreshTicks == 0) {
				menuInventory.refresh();
			}
		}
	}

}
