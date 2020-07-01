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
package me.filoghost.chestcommands.menu;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.Permissions;
import me.filoghost.chestcommands.action.Action;
import me.filoghost.chestcommands.menu.icon.AdvancedIcon;
import me.filoghost.chestcommands.util.collection.CollectionUtils;

public class AdvancedIconMenu extends BaseIconMenu<AdvancedIcon> {

	private final String fileName;
	private final String openPermission;
	
	private List<Action> openActions;
	private int refreshTicks;

	public AdvancedIconMenu(String title, int rows, String fileName) {
		super(title, rows);
		this.fileName = fileName;
		this.openPermission = Permissions.OPEN_MENU_BASE + fileName;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setOpenActions(List<Action> openAction) {
		this.openActions = CollectionUtils.nullableCopy(openAction);
	}

	public String getOpenPermission() {
		return openPermission;
	}

	public int getRefreshTicks() {
		return refreshTicks;
	}

	public void setRefreshTicks(int refreshTicks) {
		this.refreshTicks = refreshTicks;
	}

	@Override
	public void open(Player player) {
		if (openActions != null) {
			for (Action openAction : openActions) {
				openAction.execute(player);
			}
		}
		
		super.open(player);
	}
	
	public void openCheckingPermission(Player player) {
		if (player.hasPermission(openPermission)) {
			open(player);
		} else {
			sendNoOpenPermissionMessage(player);
		}
	}

	public void refresh(Player player, Inventory inventory) {
		for (int i = 0; i < inventoryGrid.getSize(); i++) {
			AdvancedIcon icon = inventoryGrid.getElementAtIndex(i);
			if (icon == null) {
				continue;
			}
			
			ItemStack newItemStack = icon.refreshItemStack(player, inventory.getItem(i));
			inventory.setItem(i, newItemStack);
		}
	}


	public void sendNoOpenPermissionMessage(CommandSender sender) {
		String noPermMessage = ChestCommands.getLang().no_open_permission;
		if (noPermMessage != null && !noPermMessage.isEmpty()) {
			sender.sendMessage(noPermMessage.replace("{permission}", this.openPermission));
		}
	}

}
