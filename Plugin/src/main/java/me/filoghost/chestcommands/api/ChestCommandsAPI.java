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

import org.bukkit.entity.Player;

import me.filoghost.chestcommands.ChestCommands;

public class ChestCommandsAPI {

	/**
	 * Checks if a menu with a given file name was loaded by the plugin.
	 *
	 * @return true - if the menu was found.
	 */
	public static boolean isPluginMenu(String yamlFile) {
		return ChestCommands.getInstance().getMenuManager().getMenuByFileName(yamlFile) != null;
	}

	/**
	 * Opens a menu loaded by ChestCommands to a player.
	 * NOTE: this method ignores permissions.
	 *
	 * @param player   - the player that will see the GUI.
	 * @param yamlFile - the file name of the menu to open. The .yml extension CANNOT be omitted.
	 * @return true - if the menu was found and opened, false if not.
	 */
	public static boolean openPluginMenu(Player player, String yamlFile) {
		IconMenu menu = ChestCommands.getInstance().getMenuManager().getMenuByFileName(yamlFile);

		if (menu != null) {
			menu.open(player);
			return true;
		} else {
			return false;
		}
	}
}
