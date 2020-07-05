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
package me.filoghost.chestcommands.inventory;

import me.filoghost.chestcommands.util.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ItemInventoryHolder implements InventoryHolder {

	private final DefaultItemInventory itemInventory;

	public ItemInventoryHolder(DefaultItemInventory itemInventory) {
		Preconditions.notNull(itemInventory, "itemInventory");
		this.itemInventory = itemInventory;
	}

	@Override
	public Inventory getInventory() {
		/*
		 * This inventory will not do anything.
		 * I'm 90% sure that it doesn't break any other plugin,
		 * because the only way you can get here is using InventoryClickEvent,
		 * that is cancelled by ChestCommands, or using InventoryOpenEvent.
		 */
		return Bukkit.createInventory(null, 9);
	}

	public DefaultItemInventory getItemInventory() {
		return itemInventory;
	}
}
