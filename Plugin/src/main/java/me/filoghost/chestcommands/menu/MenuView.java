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

import org.bukkit.inventory.Inventory;

public class MenuView {
	
	private final AdvancedIconMenu menu;
	private final Inventory inventory;
	
	public MenuView(AdvancedIconMenu menu, Inventory inventory) {
		this.menu = menu;
		this.inventory = inventory;
	}

	public AdvancedIconMenu getMenu() {
		return menu;
	}

	public Inventory getInventory() {
		return inventory;
	}		
	
}