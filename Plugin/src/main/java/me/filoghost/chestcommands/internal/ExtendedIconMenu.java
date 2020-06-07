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
package me.filoghost.chestcommands.internal;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.Permissions;
import me.filoghost.chestcommands.action.Action;
import me.filoghost.chestcommands.api.IconMenu;
import java.util.List;

public class ExtendedIconMenu extends IconMenu {

	private String fileName;
	private String permission;
	private List<Action> openActions;

	private int refreshTicks;

	public ExtendedIconMenu(String title, int rows, String fileName) {
		super(title, rows);
		this.fileName = fileName;
		this.permission = Permissions.OPEN_MENU_BASE + fileName;
	}

	public List<Action> getOpenActions() {
		return openActions;
	}

	public void setOpenActions(List<Action> openAction) {
		this.openActions = openAction;
	}

	public String getPermission() {
		return permission;
	}

	public String getFileName() {
		return fileName;
	}

	public int getRefreshTicks() {
		return refreshTicks;
	}

	public void setRefreshTicks(int refreshTicks) {
		this.refreshTicks = refreshTicks;
	}

	@Override
	public void open(Player player) {
		try {
			if (openActions != null) {
				for (Action openAction : openActions) {
					openAction.execute(player);
				}
			}

			Inventory inventory = Bukkit.createInventory(new MenuInventoryHolder(this), icons.length, title);

			for (int i = 0; i < icons.length; i++) {
				if (icons[i] != null) {

					if (icons[i] instanceof ExtendedIcon && !((ExtendedIcon) icons[i]).canViewIcon(player)) {
						continue;
					}

					inventory.setItem(i, hideAttributes(icons[i].createItemstack(player)));
				}
			}

			player.openInventory(inventory);
		} catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An internal error occurred while opening the menu. The staff should check the console for errors.");
		}
	}
	
	public void openCheckingPermission(Player player) {
		if (player.hasPermission(getPermission())) {
			open(player);
		} else {
			sendNoPermissionMessage(player);
		}
	}

	public void refresh(Player player, Inventory inventory) {
		try {
			for (int i = 0; i < icons.length; i++) {
				if (icons[i] != null && icons[i] instanceof ExtendedIcon) {
					ExtendedIcon extIcon = (ExtendedIcon) icons[i];

					if (extIcon.hasViewPermission() || extIcon.hasVariables()) {
						// Then we have to refresh it
						if (extIcon.canViewIcon(player)) {

							if (inventory.getItem(i) == null) {
								ItemStack newItem = hideAttributes(extIcon.createItemstack(player));
								inventory.setItem(i, newItem);
							} else {
								// Performance, only update name and lore
								ItemStack oldItem = hideAttributes(inventory.getItem(i));
								ItemMeta meta = oldItem.getItemMeta();
								meta.setDisplayName(extIcon.calculateName(player));
								meta.setLore(extIcon.calculateLore(player));
								oldItem.setItemMeta(meta);
							}

						} else {
							inventory.setItem(i, null);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An internal error occurred while refreshing the menu. The staff should check the console for errors.");
		}
	}

	public void sendNoPermissionMessage(CommandSender sender) {
		String noPermMessage = ChestCommands.getLang().no_open_permission;
		if (noPermMessage != null && !noPermMessage.isEmpty()) {
			sender.sendMessage(noPermMessage.replace("{permission}", this.permission));
		}
	}

}
