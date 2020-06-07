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

import me.filoghost.chestcommands.api.internal.BackendAPI;

public class ChestCommandsAPI {
	
	private ChestCommandsAPI() {}

	/**
	 * Checks if a menu with a given file name was loaded by the plugin.
	 *
	 * @return if the menu was found
	 */
	public static boolean isPluginMenu(String yamlFile) {
		return BackendAPI.getImplementation().getMenuByFileName(yamlFile) != null;
	}

	/**
	 * Opens a menu loaded by ChestCommands to a player.
	 * NOTE: this method ignores permissions.
	 *
	 * @param player the player that will see the menu
	 * @param yamlFile the file name of the menu to open (with the .yml extension)
	 * @return if the menu was found and opened
	 */
	public static boolean openPluginMenu(Player player, String yamlFile) {
		IconMenu menu = BackendAPI.getImplementation().getMenuByFileName(yamlFile);

		if (menu != null) {
			menu.open(player);
			return true;
		} else {
			return false;
		}
	}
}
