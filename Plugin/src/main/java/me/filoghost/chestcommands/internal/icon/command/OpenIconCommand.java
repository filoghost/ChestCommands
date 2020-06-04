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
package me.filoghost.chestcommands.internal.icon.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.internal.ExtendedIconMenu;
import me.filoghost.chestcommands.internal.icon.IconCommand;

public class OpenIconCommand extends IconCommand {

	public OpenIconCommand(String command) {
		super(command);
	}

	@Override
	public void execute(final Player player) {
		String target = hasVariables ? getParsedCommand(player) : command;
		final ExtendedIconMenu menu = ChestCommands.getFileNameToMenuMap().get(target.toLowerCase());
		if (menu != null) {

			/*
			 * Delay the task, since this command is executed in ClickInventoryEvent
			 * and opening another inventory in the same moment is not a good idea.
			 */
			Bukkit.getScheduler().scheduleSyncDelayedTask(ChestCommands.getInstance(), new Runnable() {
				public void run() {
					if (player.hasPermission(menu.getPermission())) {
						menu.open(player);
					} else {
						menu.sendNoPermissionMessage(player);
					}
				}
			});

		} else {
			player.sendMessage(ChatColor.RED + "Menu not found! Please inform the staff.");
		}
	}

}
