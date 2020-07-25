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
package me.filoghost.chestcommands.hook;

import me.filoghost.chestcommands.ChestCommands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public enum BungeeCordHook implements PluginHook {
	
	INSTANCE;

	@Override
	public void setup() {
		if (!Bukkit.getMessenger().isOutgoingChannelRegistered(ChestCommands.getPluginInstance(), "BungeeCord")) {
			Bukkit.getMessenger().registerOutgoingPluginChannel(ChestCommands.getPluginInstance(), "BungeeCord");
		}
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public static void connect(Player player, String server) {
		INSTANCE.checkEnabledState();
		
		if (server.length() == 0) {
			player.sendMessage(ChatColor.RED + "Target server was an empty string, cannot connect to it.");
			return;
		}

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

		try {
			dataOutputStream.writeUTF("Connect");
			dataOutputStream.writeUTF(server); // Target Server
		} catch (IOException ex) {
			throw new AssertionError();
		}

		player.sendPluginMessage(ChestCommands.getPluginInstance(), "BungeeCord", byteArrayOutputStream.toByteArray());
	}

}
