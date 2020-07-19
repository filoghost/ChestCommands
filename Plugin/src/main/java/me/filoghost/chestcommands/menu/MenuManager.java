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

import me.filoghost.chestcommands.inventory.DefaultItemInventory;
import me.filoghost.chestcommands.inventory.ItemInventoryHolder;
import me.filoghost.chestcommands.logging.ErrorMessages;
import me.filoghost.chestcommands.parsing.menu.LoadedMenu;
import me.filoghost.chestcommands.parsing.menu.OpenTrigger;
import me.filoghost.chestcommands.util.collection.CaseInsensitiveMap;
import me.filoghost.chestcommands.util.logging.ErrorCollector;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MenuManager {
	
	private static Map<String, InternalIconMenu> menusByFile;
	private static Map<String, InternalIconMenu> menusByCommand;
	private static Map<OpenTrigger, InternalIconMenu> menusByOpenTrigger;
	
	public MenuManager() {
		menusByFile = new CaseInsensitiveMap<>();
		menusByCommand = new CaseInsensitiveMap<>();
		menusByOpenTrigger = new HashMap<>();
	}
	
	public void clear() {
		menusByFile.clear();
		menusByCommand.clear();
		menusByOpenTrigger.clear();
	}

	public InternalIconMenu getMenuByFileName(String fileName) {
		return menusByFile.get(fileName);
	}

	public void registerMenu(LoadedMenu loadedMenu, ErrorCollector errorCollector) {
		InternalIconMenu menu = loadedMenu.getMenu();

		String fileName = loadedMenu.getSourceFile().getFileName().toString();
		InternalIconMenu sameNameMenu = menusByFile.get(fileName);
		if (sameNameMenu != null) {
			errorCollector.add(ErrorMessages.Menu.duplicateMenuName(sameNameMenu.getSourceFile(), loadedMenu.getSourceFile()));
		}
		menusByFile.put(fileName, menu);

		for (String triggerCommand : loadedMenu.getTriggerCommands()) {
			if (!triggerCommand.isEmpty()) {
				InternalIconMenu sameCommandMenu = menusByCommand.get(triggerCommand);
				if (sameCommandMenu != null) {
					errorCollector.add(ErrorMessages.Menu.duplicateMenuCommand(sameCommandMenu.getSourceFile(), loadedMenu.getSourceFile(), triggerCommand));
				}
				menusByCommand.put(triggerCommand, menu);
			}
		}

		if (loadedMenu.getOpenTrigger() != null) {
			menusByOpenTrigger.put(loadedMenu.getOpenTrigger(), menu);
		}
	}

	public void openMenuByItem(Player player, ItemStack itemInHand, Action clickAction) {
		menusByOpenTrigger.forEach((openTrigger, menu) -> {
			if (openTrigger.matches(itemInHand, clickAction)) {
				menu.openCheckingPermission(player);
			}
		});
	}

	public InternalIconMenu getMenuByCommand(String command) {
		return menusByCommand.get(command);
	}

	public Collection<String> getMenuFileNames() {
		return Collections.unmodifiableCollection(menusByFile.keySet());
	}
	
	public static boolean isItemInventory(Inventory inventory) {
		return getItemInventoryHolder(inventory) != null;
	}

	public static DefaultItemInventory getOpenItemInventory(Player player) {
		InventoryView view = player.getOpenInventory();
		if (view == null) {
			return null;
		}

		DefaultItemInventory itemInventory = getOpenItemInventory(view.getTopInventory());
		if (itemInventory == null) {
			itemInventory = getOpenItemInventory(view.getBottomInventory());
		}
		
		return itemInventory;
	}
	
	
	public static DefaultItemInventory getOpenItemInventory(Inventory inventory) {
		ItemInventoryHolder inventoryHolder = getItemInventoryHolder(inventory);
		if (inventoryHolder != null) {
			return inventoryHolder.getItemInventory();
		} else {
			return null;
		}
	}
	
	private static ItemInventoryHolder getItemInventoryHolder(Inventory inventory) {
		if (inventory.getHolder() instanceof ItemInventoryHolder) {
			return (ItemInventoryHolder) inventory.getHolder();
		} else {
			return null;
		}
	}

}
