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
package me.filoghost.chestcommands.parser;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.action.Action;
import me.filoghost.chestcommands.config.ConfigUtil;
import me.filoghost.chestcommands.config.yaml.PluginConfig;
import me.filoghost.chestcommands.menu.AdvancedIconMenu;
import me.filoghost.chestcommands.menu.icon.AdvancedIcon;
import me.filoghost.chestcommands.menu.settings.ClickType;
import me.filoghost.chestcommands.menu.settings.MenuSettings;
import me.filoghost.chestcommands.menu.settings.OpenTrigger;
import me.filoghost.chestcommands.parser.IconParser.Coords;
import me.filoghost.chestcommands.util.ErrorCollector;
import me.filoghost.chestcommands.util.FormatUtils;

public class MenuParser {

	private static class Nodes {

		public static final String MENU_NAME = "menu-settings.name";
		public static final String MENU_ROWS = "menu-settings.rows";
		public static final String[] MENU_COMMANDS = {"menu-settings.command", "menu-settings.commands"};

		public static final String[] OPEN_ACTIONS = {"menu-settings.open-actions", "menu-settings.open-action"};

		public static final String[] OPEN_ITEM_MATERIAL = {"menu-settings.open-with-item.id", "menu-settings.open-with-item.material"};
		public static final String OPEN_ITEM_LEFT_CLICK = "menu-settings.open-with-item.left-click";
		public static final String OPEN_ITEM_RIGHT_CLICK = "menu-settings.open-with-item.right-click";

		public static final String AUTO_REFRESH = "menu-settings.auto-refresh";

	}

	public static AdvancedIconMenu loadMenu(PluginConfig config, String title, int rows, ErrorCollector errorCollector) {
		AdvancedIconMenu iconMenu = new AdvancedIconMenu(title, rows, config.getFileName());

		for (String subSectionName : config.getKeys(false)) {
			if (subSectionName.equals("menu-settings")) {
				continue;
			}

			ConfigurationSection iconSection = config.getConfigurationSection(subSectionName);

			AdvancedIcon icon = IconParser.loadIconFromSection(iconSection, subSectionName, config.getFileName(), errorCollector);
			Coords coords = IconParser.loadCoordsFromSection(iconSection);
			
			int actualX = coords.getX() - 1;
			int actualY = coords.getY() - 1;

			if (!coords.isSetX() || !coords.isSetY()) {
				errorCollector.addError("The icon \"" + subSectionName + "\" in the menu \"" + config.getFileName() + " is missing POSITION-X and/or POSITION-Y.");
				continue;
			}			
			if (actualX < 0 || actualX >= iconMenu.getColumnCount()) {
				errorCollector.addError("The icon \"" + subSectionName + "\" in the menu \"" + config.getFileName() + " has an invalid POSITION-X: it must be between 1 and " + iconMenu.getColumnCount() + " (was " + coords.getX() + ").");
				continue;
			}
			if (actualY < 0 || actualY >= iconMenu.getRowCount()) {
				errorCollector.addError("The icon \"" + subSectionName + "\" in the menu \"" + config.getFileName() + " has an invalid POSITION-Y: it must be between 1 and " + iconMenu.getRowCount() + " (was " + coords.getY() + ").");
				continue;
			}
			
			if (iconMenu.getIcon(actualX, actualY) != null) {
				errorCollector.addError("The icon \"" + subSectionName + "\" in the menu \"" + config.getFileName() + " is overriding another icon with the same position.");
			}

			iconMenu.setIcon(actualX, actualY, icon);
		}

		return iconMenu;
	}

	/**
	 * Reads all the settings of a menu. It will never return a null title, even if not set.
	 */
	public static MenuSettings loadMenuSettings(PluginConfig config, ErrorCollector errorCollector) {

		String title = FormatUtils.addColors(config.getString(Nodes.MENU_NAME));
		int rows;

		if (title == null) {
			errorCollector.addError("The menu \"" + config.getFileName() + "\" doesn't have a name set.");
			title = ChatColor.DARK_RED + "No title set";
		}

		if (title.length() > 32) {
			title = title.substring(0, 32);
		}

		if (config.isInt(Nodes.MENU_ROWS)) {
			rows = config.getInt(Nodes.MENU_ROWS);

			if (rows <= 0) {
				rows = 1;
			}

		} else {
			rows = 6; // Defaults to 6 rows
			errorCollector.addError("The menu \"" + config.getFileName() + "\" doesn't have a the number of rows set, it will have 6 rows by default.");
		}

		MenuSettings menuSettings = new MenuSettings(title, rows);
		
		List<String> triggeringCommands = ConfigUtil.getStringListOrInlineList(config, ";", Nodes.MENU_COMMANDS);
		if (triggeringCommands != null) {
			menuSettings.setCommands(triggeringCommands);
		}

		List<String> serializedOpenActions = ConfigUtil.getStringListOrInlineList(config, ChestCommands.getSettings().multiple_commands_separator, Nodes.OPEN_ACTIONS);
		
		if (serializedOpenActions != null && !serializedOpenActions.isEmpty()) {
			List<Action> openActions = new ArrayList<>();
			
			for (String serializedAction : serializedOpenActions) {
				if (serializedAction != null && !serializedAction.isEmpty()) {
					openActions.add(ActionParser.parseAction(serializedAction));
				}
			}

			if (!openActions.isEmpty()) {
				menuSettings.setOpenActions(openActions);
			}
		}

		String openItemMaterial = ConfigUtil.getAnyString(config, Nodes.OPEN_ITEM_MATERIAL);
		if (openItemMaterial != null) {
			boolean leftClick = config.getBoolean(Nodes.OPEN_ITEM_LEFT_CLICK);
			boolean rightClick = config.getBoolean(Nodes.OPEN_ITEM_RIGHT_CLICK);
			
			if (leftClick || rightClick) {
				try {
					ItemStackParser itemReader = new ItemStackParser(openItemMaterial, false);
					ClickType clickType = ClickType.fromOptions(leftClick, rightClick);
					
					OpenTrigger openTrigger = new OpenTrigger(itemReader.getMaterial(), clickType);
					
					if (itemReader.hasExplicitDurability()) {
						openTrigger.setRestrictiveDurability(itemReader.getDurability());
					}
					
				} catch (ParseException e) {
					errorCollector.addError("The item \"" + openItemMaterial + "\" used to open the menu \"" + config.getFileName() + "\" is invalid: " + e.getMessage());
				}
			}
		}

		if (config.isSet(Nodes.AUTO_REFRESH)) {
			int tenthsToRefresh = (int) (config.getDouble(Nodes.AUTO_REFRESH) * 10.0);
			if (tenthsToRefresh < 1) {
				tenthsToRefresh = 1;
			}
			menuSettings.setRefreshTenths(tenthsToRefresh);
		}

		return menuSettings;
	}

}
