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

import me.filoghost.chestcommands.config.files.LoadedMenu;
import me.filoghost.chestcommands.menu.inventory.MenuInventoryHolder;
import me.filoghost.chestcommands.menu.settings.OpenTrigger;
import me.filoghost.chestcommands.util.CaseInsensitiveMap;
import me.filoghost.chestcommands.util.ErrorCollector;
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
	
	private static Map<String, AdvancedIconMenu> menusByFile;
	private static Map<String, AdvancedIconMenu> menusByCommand;
	private static Map<OpenTrigger, AdvancedIconMenu> menusByOpenTrigger;
	
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

	public AdvancedIconMenu getMenuByFileName(String fileName) {
		return menusByFile.get(fileName);
	}

	public void registerMenu(LoadedMenu loadedMenu, ErrorCollector errorCollector) {
		AdvancedIconMenu menu = loadedMenu.getMenu();

		if (menusByFile.containsKey(loadedMenu.getFileName())) {
			errorCollector.addError("Two menus have the same file name \"" + loadedMenu.getFileName() + "\". "
					+ "One of them will not work.");
		}
		
		menusByFile.put(loadedMenu.getFileName(), menu);

		for (String triggerCommand : loadedMenu.getTriggerCommands()) {
			if (!triggerCommand.isEmpty()) {
				if (menusByCommand.containsKey(triggerCommand)) {
					errorCollector.addError("The menus \"" + menusByCommand.get(triggerCommand).getFileName() + "\" "
							+ "and \"" + loadedMenu.getFileName() + "\" have the same command \"" + triggerCommand + "\". "
							+ "Only one will be opened.");
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

	public AdvancedIconMenu getMenuByCommand(String command) {
		return menusByCommand.get(command);
	}

	public Collection<String> getMenuFileNames() {
		return Collections.unmodifiableCollection(menusByFile.keySet());
	}
	
	public static boolean isMenuInventory(Inventory inventory) {
		return getMenuInventoryHolder(inventory) != null;
	}
	
	public static BaseIconMenu<?> getOpenMenu(Player player) {
		MenuView menuView = getOpenMenuView(player);
		if (menuView != null) {
			return menuView.getMenu();
		} else {
			return null;
		}
	}
	
	public static BaseIconMenu<?> getOpenMenu(Inventory inventory) {
		MenuView menuView = getOpenMenuView(inventory);
		if (menuView != null) {
			return menuView.getMenu();
		} else {
			return null;
		}
	}
	
	public static MenuView getOpenMenuView(Player player) {
		InventoryView view = player.getOpenInventory();
		if (view == null) {
			return null;
		}
		
		MenuView openMenuView = getOpenMenuView(view.getTopInventory());
		if (openMenuView == null) {
			openMenuView = getOpenMenuView(view.getBottomInventory());
		}
		
		return openMenuView;
	}
	
	
	public static MenuView getOpenMenuView(Inventory inventory) {
		MenuInventoryHolder inventoryHolder = getMenuInventoryHolder(inventory);
		if (inventoryHolder != null) {
			return new MenuView(inventoryHolder.getIconMenu(), inventory);
		} else {
			return null;
		}
	}
	
	private static MenuInventoryHolder getMenuInventoryHolder(Inventory inventory) {
		if (inventory.getHolder() instanceof MenuInventoryHolder) {
			return (MenuInventoryHolder) inventory.getHolder();
		} else {
			return null;
		}
	}

}
