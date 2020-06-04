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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import me.filoghost.chestcommands.api.IconMenu;
import me.filoghost.chestcommands.util.Validate;

/**
 * This class links an IconMenu with an Inventory, via InventoryHolder.
 */
public class MenuInventoryHolder implements InventoryHolder {

	private IconMenu iconMenu;

	public MenuInventoryHolder(IconMenu iconMenu) {
		this.iconMenu = iconMenu;
	}

	@Override
	public Inventory getInventory() {
		/*
		 * This inventory will not do anything.
		 * I'm 90% sure that it doesn't break any other plugin,
		 * because the only way you can get here is using InventoryClickEvent,
		 * that is cancelled by ChestCommands, or using InventoryOpenEvent.
		 */
		return Bukkit.createInventory(null, iconMenu.getSize());
	}

	public IconMenu getIconMenu() {
		return iconMenu;
	}

	public void setIconMenu(IconMenu iconMenu) {
		Validate.notNull(iconMenu, "IconMenu cannot be null");
		this.iconMenu = iconMenu;
	}

}
