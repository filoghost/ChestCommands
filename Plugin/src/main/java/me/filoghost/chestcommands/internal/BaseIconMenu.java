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
package me.filoghost.chestcommands.internal;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.filoghost.chestcommands.api.Icon;
import me.filoghost.chestcommands.util.Preconditions;
import me.filoghost.chestcommands.util.Utils;

public class BaseIconMenu<T extends Icon> {

    
	protected final String title;
	protected final Grid<T> inventoryGrid;


	public BaseIconMenu(String title, int rows) {
		this.title = title;
		this.inventoryGrid = new Grid<>(rows, 9);
	}

	public void setIcon(int x, int y, T icon) {
		inventoryGrid.setElement(x, y, icon);
	}

	public T getIcon(int x, int y) {
		return inventoryGrid.getElement(x, y);
	}
	
	public T getIconAtSlot(int slot) {
		return inventoryGrid.getElementAtIndex(slot);
	}
	
	public int getRowCount() {
		return inventoryGrid.getRows();
	}
	
	public int getColumnCount() {
		return inventoryGrid.getColumns();
	}
	
	public int getSize() {
		return inventoryGrid.getSize();
	}

	public String getTitle() {
		return title;
	}


	public void open(Player player) {
		Preconditions.notNull(player, "player");

		Inventory inventory = Bukkit.createInventory(new MenuInventoryHolder(this), getSize(), title);

		for (int i = 0; i < inventoryGrid.getSize(); i++) {
			Icon icon = inventoryGrid.getElementAtIndex(i);
			if (icon != null && canViewIcon(player, icon)) {
				inventory.setItem(i, hideAttributes(icon.createItemStack(player)));
			}
		}

		player.openInventory(inventory);
	}
	
	protected boolean canViewIcon(Player player, Icon icon) {
		return true;
	}
	
	protected ItemStack hideAttributes(ItemStack item) {
		if (item == null) {
			return null;
		}

		ItemMeta meta = item.getItemMeta();
		if (Utils.isNullOrEmpty(meta.getItemFlags())) {
			// Add them only if no flag was already set
			meta.addItemFlags(ItemFlag.values());
			item.setItemMeta(meta);
		}
		return item;
	}

	@Override
	public String toString() {
		return "IconMenu [title=" + title + ", icons=" + inventoryGrid + "]";
	}
	
}
