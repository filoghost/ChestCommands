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

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.filoghost.chestcommands.MenuManager;
import me.filoghost.chestcommands.internal.ExtendedIconMenu;
import me.filoghost.chestcommands.util.StringUtils;

public class CommandListener implements Listener {
	
	private MenuManager menuManager;
	
	public CommandListener(MenuManager menuManager) {
		this.menuManager = menuManager;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		// Very fast method compared to split & substring
		String command = StringUtils.getCleanCommand(event.getMessage());

		if (command.isEmpty()) {
			return;
		}

		ExtendedIconMenu menu = menuManager.getMenuByCommand(command);

		if (menu != null) {
			event.setCancelled(true);
			menu.openCheckingPermission(event.getPlayer());
		}
	}

}
