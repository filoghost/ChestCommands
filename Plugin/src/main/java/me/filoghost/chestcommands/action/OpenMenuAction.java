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
package me.filoghost.chestcommands.action;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.logging.ErrorMessages;
import me.filoghost.chestcommands.menu.InternalIconMenu;
import me.filoghost.chestcommands.placeholder.PlaceholderString;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class OpenMenuAction implements Action {

	private final PlaceholderString targetMenu;
	
	public OpenMenuAction(String serializedAction) {
		targetMenu = PlaceholderString.of(serializedAction);
	}

	@Override
	public void execute(final Player player) {
		String menuName = targetMenu.getValue(player);
		final InternalIconMenu menu = ChestCommands.getMenuManager().getMenuByFileName(menuName);
		
		if (menu != null) {
			/*
			 * Delay the task, since this action is executed in ClickInventoryEvent
			 * and opening another inventory in the same moment is not a good idea.
			 */
			Bukkit.getScheduler().runTask(ChestCommands.getPluginInstance(), () -> {
				menu.openCheckingPermission(player);
			});

		} else {
			player.sendMessage(ErrorMessages.User.configurationError("couldn't find the menu \"" + menuName + "\""));
		}
	}

}
