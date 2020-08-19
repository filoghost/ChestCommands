/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.menu;

import me.filoghost.chestcommands.inventory.DefaultMenuView;
import me.filoghost.chestcommands.inventory.MenuInventoryHolder;
import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.chestcommands.parsing.menu.LoadedMenu;
import me.filoghost.chestcommands.parsing.menu.MenuOpenItem;
import me.filoghost.fcommons.collection.CaseInsensitiveMap;
import me.filoghost.fcommons.logging.ErrorCollector;
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
	
	private static Map<String, InternalMenu> menusByFile;
	private static Map<String, InternalMenu> menusByOpenCommand;
	private static Map<MenuOpenItem, InternalMenu> menusByOpenItem;
	
	public MenuManager() {
		menusByFile = new CaseInsensitiveMap<>();
		menusByOpenCommand = new CaseInsensitiveMap<>();
		menusByOpenItem = new HashMap<>();
	}
	
	public void clear() {
		menusByFile.clear();
		menusByOpenCommand.clear();
		menusByOpenItem.clear();
	}

	public InternalMenu getMenuByFileName(String fileName) {
		return menusByFile.get(fileName);
	}

	public void registerMenu(LoadedMenu loadedMenu, ErrorCollector errorCollector) {
		InternalMenu menu = loadedMenu.getMenu();

		String fileName = loadedMenu.getSourceFile().getFileName().toString();
		InternalMenu sameNameMenu = menusByFile.get(fileName);
		if (sameNameMenu != null) {
			errorCollector.add(Errors.Menu.duplicateMenuName(sameNameMenu.getSourceFile(), loadedMenu.getSourceFile()));
		}
		menusByFile.put(fileName, menu);

		if (loadedMenu.getOpenCommands() != null) {
			for (String openCommand : loadedMenu.getOpenCommands()) {
				if (!openCommand.isEmpty()) {
					InternalMenu sameCommandMenu = menusByOpenCommand.get(openCommand);
					if (sameCommandMenu != null) {
						errorCollector.add(Errors.Menu.duplicateMenuCommand(sameCommandMenu.getSourceFile(), loadedMenu.getSourceFile(), openCommand));
					}
					menusByOpenCommand.put(openCommand, menu);
				}
			}
		}

		if (loadedMenu.getOpenItem() != null) {
			menusByOpenItem.put(loadedMenu.getOpenItem(), menu);
		}
	}

	public void openMenuByItem(Player player, ItemStack itemInHand, Action clickAction) {
		menusByOpenItem.forEach((openItem, menu) -> {
			if (openItem.matches(itemInHand, clickAction)) {
				menu.openCheckingPermission(player);
			}
		});
	}

	public InternalMenu getMenuByOpenCommand(String openCommand) {
		return menusByOpenCommand.get(openCommand);
	}

	public Collection<String> getMenuFileNames() {
		return Collections.unmodifiableCollection(menusByFile.keySet());
	}
	
	public static boolean isMenuInventory(Inventory inventory) {
		return getMenuInventoryHolder(inventory) != null;
	}

	public static DefaultMenuView getOpenMenuView(Player player) {
		InventoryView view = player.getOpenInventory();
		if (view == null) {
			return null;
		}

		DefaultMenuView menuView = getOpenMenuView(view.getTopInventory());
		if (menuView == null) {
			menuView = getOpenMenuView(view.getBottomInventory());
		}
		
		return menuView;
	}
	
	
	public static DefaultMenuView getOpenMenuView(Inventory inventory) {
		MenuInventoryHolder inventoryHolder = getMenuInventoryHolder(inventory);
		if (inventoryHolder != null) {
			return inventoryHolder.getMenuView();
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
