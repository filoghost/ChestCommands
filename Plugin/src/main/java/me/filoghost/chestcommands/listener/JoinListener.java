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
package me.filoghost.chestcommands.listener;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {

		if (ChestCommands.getLastLoadErrors().hasErrors() && event.getPlayer().hasPermission(Permissions.SEE_ERRORS)) {
			event.getPlayer().sendMessage(ChestCommands.CHAT_PREFIX + ChatColor.RED + "The plugin found " + ChestCommands.getLastLoadErrors().getErrorsCount() + " error(s) last time it was loaded. You can see them by doing \"/cc reload\" in the console.");
		}

		if (ChestCommands.hasNewVersion() && ChestCommands.getSettings().update_notifications && event.getPlayer().hasPermission(Permissions.UPDATE_NOTIFICATIONS)) {
			event.getPlayer().sendMessage(ChestCommands.CHAT_PREFIX + "Found an update: " + ChestCommands.getNewVersion() + ". Download:");
			event.getPlayer().sendMessage(ChatColor.DARK_GREEN + ">> " + ChatColor.GREEN + "http://dev.bukkit.org/bukkit-plugins/chest-commands");
		}
	}

}
