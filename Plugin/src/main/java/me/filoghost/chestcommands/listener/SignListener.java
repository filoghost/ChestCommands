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

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.Permissions;
import me.filoghost.chestcommands.api.IconMenu;
import me.filoghost.chestcommands.internal.ExtendedIconMenu;
import me.filoghost.chestcommands.util.BukkitUtils;
import me.filoghost.chestcommands.util.MaterialsRegistry;

public class SignListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onInteract(PlayerInteractEvent event) {

		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && MaterialsRegistry.isSign(event.getClickedBlock().getType())) {

			Sign sign = (Sign) event.getClickedBlock().getState();
			if (sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_BLUE + "[menu]")) {

				sign.getLine(1);
				ExtendedIconMenu iconMenu = ChestCommands.getFileNameToMenuMap().get(BukkitUtils.addYamlExtension(sign.getLine(1)));
				if (iconMenu != null) {

					if (event.getPlayer().hasPermission(iconMenu.getPermission())) {
						iconMenu.open(event.getPlayer());
					} else {
						iconMenu.sendNoPermissionMessage(event.getPlayer());
					}

				} else {
					sign.setLine(0, ChatColor.RED + ChatColor.stripColor(sign.getLine(0)));
					event.getPlayer().sendMessage(ChestCommands.getLang().menu_not_found);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onSignChange(SignChangeEvent event) {
		if (event.getLine(0).equalsIgnoreCase("[menu]") && event.getPlayer().hasPermission(Permissions.SIGN_CREATE)) {

			if (event.getLine(1).isEmpty()) {
				event.setLine(0, ChatColor.RED + event.getLine(0));
				event.getPlayer().sendMessage(ChatColor.RED + "You must set a valid menu name in the second line.");
				return;
			}

			IconMenu iconMenu = ChestCommands.getFileNameToMenuMap().get(BukkitUtils.addYamlExtension(event.getLine(1)));
			if (iconMenu == null) {
				event.setLine(0, ChatColor.RED + event.getLine(0));
				event.getPlayer().sendMessage(ChatColor.RED + "That menu was not found.");
				return;
			}

			event.setLine(0, ChatColor.DARK_BLUE + event.getLine(0));
			event.getPlayer().sendMessage(ChatColor.GREEN + "Successfully created a sign for the menu " + BukkitUtils.addYamlExtension(event.getLine(1)) + ".");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onSignChangeMonitor(SignChangeEvent event) {
		// Prevent players with permissions for creating colored signs from creating menu signs
		if (event.getLine(0).equalsIgnoreCase(ChatColor.DARK_BLUE + "[menu]") && !event.getPlayer().hasPermission(Permissions.SIGN_CREATE)) {
			event.setLine(0, ChatColor.stripColor(event.getLine(0)));
		}
	}

}
