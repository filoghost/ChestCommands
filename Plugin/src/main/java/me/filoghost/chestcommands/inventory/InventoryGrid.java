/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.chestcommands.inventory;

import me.filoghost.chestcommands.util.collection.Grid;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryGrid extends Grid<ItemStack> {

	private final Inventory inventory;

	public InventoryGrid(MenuInventoryHolder inventoryHolder, int rows, String title) {
		super(rows, 9);
		this.inventory = Bukkit.createInventory(inventoryHolder, getSize(), title);
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	protected ItemStack getByIndex0(int ordinalIndex) {
		return inventory.getItem(ordinalIndex);
	}

	@Override
	protected void setByIndex0(int ordinalIndex, ItemStack element) {
		inventory.setItem(ordinalIndex, element);
	}

}
