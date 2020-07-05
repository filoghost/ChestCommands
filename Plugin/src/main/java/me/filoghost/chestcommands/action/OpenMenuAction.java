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
package me.filoghost.chestcommands.action;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.menu.InternalIconMenu;
import me.filoghost.chestcommands.placeholder.RelativeString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class OpenMenuAction extends Action {

	private final RelativeString targetMenu;
	
	public OpenMenuAction(String serializedAction) {
		targetMenu = RelativeString.of(serializedAction);
	}

	@Override
	protected void execute0(final Player player) {
		final InternalIconMenu menu = ChestCommands.getInstance().getMenuManager().getMenuByFileName(targetMenu.getValue(player));
		
		if (menu != null) {
			/*
			 * Delay the task, since this action is executed in ClickInventoryEvent
			 * and opening another inventory in the same moment is not a good idea.
			 */
			Bukkit.getScheduler().runTask(ChestCommands.getInstance(), () -> {
				menu.openCheckingPermission(player);
			});

		} else {
			player.sendMessage(ChatColor.RED + "Menu not found! Please inform the staff.");
		}
	}

}
