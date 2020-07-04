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
package me.filoghost.chestcommands.listener;

import me.filoghost.chestcommands.menu.InternalIconMenu;
import me.filoghost.chestcommands.menu.MenuManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {
	
	private final MenuManager menuManager;
	
	public CommandListener(MenuManager menuManager) {
		this.menuManager = menuManager;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		String command = getCommandName(event.getMessage());
		
		if (command == null) {
			return;
		}

		InternalIconMenu menu = menuManager.getMenuByCommand(command);
		
		if (menu == null) {
			return;
		}
		
		event.setCancelled(true);
		menu.openCheckingPermission(event.getPlayer());
	}
	
	private static String getCommandName(String fullCommand) {
		if (!fullCommand.startsWith("/")) {
			return null;
		}
		
		int firstSpace = fullCommand.indexOf(' ');
		if (firstSpace >= 1) {
			return fullCommand.substring(1, firstSpace);
		} else {
			return fullCommand.substring(1);
		}
	}

}
