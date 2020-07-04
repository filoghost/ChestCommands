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
package me.filoghost.chestcommands.api;

import me.filoghost.chestcommands.api.internal.BackendAPI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface IconMenu {
	
	static IconMenu create(Plugin owner, String title, int rowCount) {
		return BackendAPI.getImplementation().createIconMenu(owner, title, rowCount);
	}

	void setIcon(int row, int column, Icon icon);

	Icon getIcon(int row, int column);

	String getTitle();
	
	int getRowCount();
	
	int getColumnCount();

	/**
	 * Opens a view of the current menu configuration.
	 * Updating the menu doesn't automatically update all the views.
	 *
	 * @param player the player to which the menu will be displayed
	 */
	ItemInventory open(Player player);

}