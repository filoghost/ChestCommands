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
package me.filoghost.chestcommands;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import me.filoghost.chestcommands.api.IconMenu;
import me.filoghost.chestcommands.internal.BoundItem;
import me.filoghost.chestcommands.internal.ExtendedIconMenu;
import me.filoghost.chestcommands.internal.MenuInventoryHolder;
import me.filoghost.chestcommands.util.CaseInsensitiveMap;
import me.filoghost.chestcommands.util.ErrorCollector;

public class MenuManager {
	
	private static Map<String, ExtendedIconMenu> fileNameToMenuMap;
	private static Map<String, ExtendedIconMenu> commandsToMenuMap;

	private static Set<BoundItem> boundItems;
	
	public MenuManager() {
		fileNameToMenuMap = CaseInsensitiveMap.create();
		commandsToMenuMap = CaseInsensitiveMap.create();
		boundItems = new HashSet<>();
	}
	
	public void clear() {
		fileNameToMenuMap.clear();
		commandsToMenuMap.clear();
		boundItems.clear();
	}

	public ExtendedIconMenu getMenuByFileName(String fileName) {
		return fileNameToMenuMap.get(fileName);
	}

	public void registerMenu(String fileName, String[] triggerCommands, ExtendedIconMenu menu, ErrorCollector errorCollector) {
		if (fileNameToMenuMap.containsKey(fileName)) {
			errorCollector.addError("Two menus have the same file name \"" + fileName + "\" with different cases. There will be problems opening one of these two menus.");
		}
		
		fileNameToMenuMap.put(fileName, menu);

		for (String triggerCommand : triggerCommands) {
			if (!triggerCommand.isEmpty()) {
				if (commandsToMenuMap.containsKey(triggerCommand)) {
					errorCollector.addError("The menus \"" + commandsToMenuMap.get(triggerCommand).getFileName() + "\" and \"" + fileName + "\" have the same command \"" + triggerCommand + "\". Only one will be opened.");
				}
				commandsToMenuMap.put(triggerCommand, menu);
			}
		}		
	}

	public void registerTriggerItem(BoundItem boundItem) {
		boundItems.add(boundItem);
	}

	public void openMenuByItem(Player player, ItemStack itemInHand, Action clickAction) {
		for (BoundItem boundItem : boundItems) {
			if (boundItem.isValidTrigger(itemInHand, clickAction)) {
				boundItem.getMenu().openCheckingPermission(player);
			}
		}
	}
	
	public IconMenu getIconMenu(Inventory inventory) {
		if (inventory.getHolder() instanceof MenuInventoryHolder) {
			return ((MenuInventoryHolder) inventory.getHolder()).getIconMenu();
		} else {
			return null;
		}
	}

	public ExtendedIconMenu getMenuByCommand(String command) {
		return commandsToMenuMap.get(command);
	}

	public Collection<String> getMenuFileNames() {
		return Collections.unmodifiableCollection(fileNameToMenuMap.keySet());
	}
	
	
	public static void refreshOpenMenu(Player player) {
		MenuView openMenu = getOpenMenu(player);
		if (openMenu != null) {
			openMenu.getMenu().refresh(player, openMenu.getInventory());
		}
	}
	
	
	public static MenuView getOpenMenu(Player player) {
		InventoryView view = player.getOpenInventory();
		if (view == null) {
			return null;
		}
		
		MenuView openMenu = getOpenMenu(view.getTopInventory());
		if (openMenu == null) {
			openMenu = getOpenMenu(view.getBottomInventory());
		}
		
		return openMenu;
	}
	
	
	private static MenuView getOpenMenu(Inventory inventory) {
		if (!(inventory.getHolder() instanceof MenuInventoryHolder)) {
			return null;
		}
		
		MenuInventoryHolder menuInventoryHolder = (MenuInventoryHolder) inventory.getHolder();
		if (!(menuInventoryHolder.getIconMenu() instanceof ExtendedIconMenu)) {
			return null;
		}
			
		return new MenuView((ExtendedIconMenu) menuInventoryHolder.getIconMenu(), inventory);		
	}
	
	
	public static class MenuView {
		
		private final ExtendedIconMenu menu;
		private final Inventory inventory;
		
		public MenuView(ExtendedIconMenu menu, Inventory inventory) {
			this.menu = menu;
			this.inventory = inventory;
		}

		public ExtendedIconMenu getMenu() {
			return menu;
		}

		public Inventory getInventory() {
			return inventory;
		}		
		
	}

}
