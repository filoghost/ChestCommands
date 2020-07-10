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
package me.filoghost.chestcommands.menu;

import me.filoghost.chestcommands.api.Icon;
import me.filoghost.chestcommands.api.IconMenu;
import me.filoghost.chestcommands.api.ItemInventory;
import me.filoghost.chestcommands.inventory.DefaultItemInventory;
import me.filoghost.chestcommands.util.Preconditions;
import me.filoghost.chestcommands.util.collection.ArrayGrid;
import me.filoghost.chestcommands.util.collection.Grid;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class BaseIconMenu implements IconMenu {

    
	protected final String title;
	protected final Grid<Icon> icons;


	public BaseIconMenu(String title, int rows) {
		this.title = title;
		this.icons = new ArrayGrid<>(rows, 9);
	}

	@Override
	public void setIcon(int row, int column, Icon icon) {
		icons.set(row, column, icon);
	}

	@Override
	public Icon getIcon(int row, int column) {
		return icons.get(row, column);
	}

	@Override
	public int getRowCount() {
		return icons.getRows();
	}
	
	@Override
	public int getColumnCount() {
		return icons.getColumns();
	}

	@Override
	public String getTitle() {
		return title;
	}

	public Grid<Icon> getIcons() {
		return icons;
	}

	@Override
	public ItemInventory open(Player player) {
		Preconditions.notNull(player, "player");

		DefaultItemInventory itemInventory = new DefaultItemInventory(this, player);
		itemInventory.open(player);
		return itemInventory;
	}

	@Override
	public void refreshOpenInventories() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			DefaultItemInventory itemInventory = MenuManager.getOpenItemInventory(player);
			if (itemInventory.getIconMenu() == this) {
				itemInventory.refresh();
			}
		}
	}
}
