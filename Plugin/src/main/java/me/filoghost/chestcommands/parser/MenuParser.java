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

import me.filoghost.chestcommands.action.Action;
import me.filoghost.chestcommands.config.yaml.Config;
import me.filoghost.chestcommands.menu.AdvancedIconMenu;
import me.filoghost.chestcommands.menu.icon.AdvancedIcon;
import me.filoghost.chestcommands.menu.settings.ClickType;
import me.filoghost.chestcommands.menu.settings.MenuSettings;
import me.filoghost.chestcommands.menu.settings.OpenTrigger;
import me.filoghost.chestcommands.parser.IconParser.Coords;
import me.filoghost.chestcommands.util.ErrorCollector;
import me.filoghost.chestcommands.util.FormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class MenuParser {

	public static class Nodes {

		public static final String MENU_SETTINGS = "menu-settings";

		public static final String NAME = "name";
		public static final String ROWS = "rows";
		public static final String COMMANDS = "commands";

		public static final String OPEN_ACTIONS = "open-actions";

		public static final String OPEN_ITEM_MATERIAL = "open-with-item.material";
		public static final String OPEN_ITEM_LEFT_CLICK = "open-with-item.left-click";
		public static final String OPEN_ITEM_RIGHT_CLICK = "open-with-item.right-click";

		public static final String AUTO_REFRESH = "auto-refresh";

	}

	public static AdvancedIconMenu loadMenu(Config config, String title, int rows, ErrorCollector errorCollector) {
		AdvancedIconMenu iconMenu = new AdvancedIconMenu(title, rows, config.getFileName());

		for (String subSectionName : config.getKeys(false)) {
			if (subSectionName.equals(Nodes.MENU_SETTINGS)) {
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
	public static MenuSettings loadMenuSettings(Config config, ErrorCollector errorCollector) {
		ConfigurationSection settingsSection = config.getConfigurationSection(Nodes.MENU_SETTINGS);

		String title = FormatUtils.addColors(settingsSection.getString(Nodes.NAME));
		int rows;

		if (title == null) {
			errorCollector.addError("The menu \"" + config.getFileName() + "\" doesn't have a name set.");
			title = ChatColor.DARK_RED + "No title set";
		}

		if (title.length() > 32) {
			title = title.substring(0, 32);
		}

		if (settingsSection.isInt(Nodes.ROWS)) {
			rows = settingsSection.getInt(Nodes.ROWS);

			if (rows <= 0) {
				rows = 1;
			}

		} else {
			rows = 6; // Defaults to 6 rows
			errorCollector.addError("The menu \"" + config.getFileName() + "\" doesn't have a the number of rows set, it will have 6 rows by default.");
		}

		MenuSettings menuSettings = new MenuSettings(title, rows);
		
		List<String> triggeringCommands = settingsSection.getStringList(Nodes.COMMANDS);
		if (triggeringCommands != null) {
			menuSettings.setCommands(triggeringCommands);
		}

		List<String> serializedOpenActions = settingsSection.getStringList(Nodes.OPEN_ACTIONS);
		
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

		String openItemMaterial = settingsSection.getString(Nodes.OPEN_ITEM_MATERIAL);
		if (openItemMaterial != null) {
			boolean leftClick = settingsSection.getBoolean(Nodes.OPEN_ITEM_LEFT_CLICK);
			boolean rightClick = settingsSection.getBoolean(Nodes.OPEN_ITEM_RIGHT_CLICK);
			
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

		if (settingsSection.isSet(Nodes.AUTO_REFRESH)) {
			int tenthsToRefresh = (int) (settingsSection.getDouble(Nodes.AUTO_REFRESH) * 10.0);
			if (tenthsToRefresh < 1) {
				tenthsToRefresh = 1;
			}
			menuSettings.setRefreshTenths(tenthsToRefresh);
		}

		return menuSettings;
	}

}
