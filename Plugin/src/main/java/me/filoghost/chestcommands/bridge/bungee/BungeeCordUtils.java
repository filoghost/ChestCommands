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
package me.filoghost.chestcommands.bridge.bungee;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.filoghost.chestcommands.ChestCommands;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class BungeeCordUtils {

	public static boolean connect(Player player, String server) {

		try {

			if (server.length() == 0) {
				player.sendMessage("§cTarget server was \"\" (empty string) cannot connect to it.");
				return false;
			}

			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(byteArray);

			out.writeUTF("Connect");
			out.writeUTF(server); // Target Server

			player.sendPluginMessage(ChestCommands.getInstance(), "BungeeCord", byteArray.toByteArray());

		} catch (Exception ex) {
			player.sendMessage(ChatColor.RED + "An unexpected exception has occurred. Please notify the server's staff about this. (They should look at the console).");
			ex.printStackTrace();
			ChestCommands.getInstance().getLogger().warning("Could not connect \"" + player.getName() + "\" to the server \"" + server + "\".");
			return false;
		}

		return true;
	}

}
