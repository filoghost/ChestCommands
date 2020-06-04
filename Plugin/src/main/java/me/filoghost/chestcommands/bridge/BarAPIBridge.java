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
package me.filoghost.chestcommands.bridge;

import me.confuser.barapi.BarAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BarAPIBridge {

	private static BarAPI barAPI;

	public static boolean setupPlugin() {
		Plugin barPlugin = Bukkit.getPluginManager().getPlugin("BarAPI");

		if (barPlugin == null) {
			return false;
		}

		barAPI = (BarAPI) barPlugin;
		return true;
	}

	public static boolean hasValidPlugin() {
		return barAPI != null;
	}

	public static void setMessage(Player player, String message, int seconds) {
		if (!hasValidPlugin()) throw new IllegalStateException("BarAPI plugin was not found!");

		BarAPI.setMessage(player, message, seconds);
	}

}
