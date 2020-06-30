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
import me.filoghost.chestcommands.config.Config;
import me.filoghost.chestcommands.config.ConfigSection;
import me.filoghost.chestcommands.config.ConfigValueException;
import me.filoghost.chestcommands.config.files.LoadedMenu;
import me.filoghost.chestcommands.menu.AdvancedIconMenu;
import me.filoghost.chestcommands.menu.icon.AdvancedIcon;
import me.filoghost.chestcommands.menu.settings.ClickType;
import me.filoghost.chestcommands.menu.settings.MenuSettings;
import me.filoghost.chestcommands.menu.settings.OpenTrigger;
import me.filoghost.chestcommands.parser.icon.IconNode;
import me.filoghost.chestcommands.parser.icon.IconSettings;
import me.filoghost.chestcommands.util.ErrorCollector;
import me.filoghost.chestcommands.util.FormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;

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


	public static LoadedMenu loadMenu(Config menuConfig, ErrorCollector errorCollector) {
		MenuSettings menuSettings = loadMenuSettings(menuConfig, errorCollector);
		List<IconSettings> iconSettingsList = loadIconSettingsList(menuConfig, errorCollector);

		AdvancedIconMenu iconMenu = new AdvancedIconMenu(menuSettings.getTitle(), menuSettings.getRows(), menuConfig.getFileName());

		for (IconSettings iconSettings : iconSettingsList) {
			try {
				addIconToMenu(iconMenu, iconSettings, errorCollector);
			} catch (IconAddException e) {
				errorCollector.addError(e.getMessage());
			}
		}

		iconMenu.setRefreshTicks(menuSettings.getRefreshTenths());
		iconMenu.setOpenActions(menuSettings.getOpenActions());

		return new LoadedMenu(iconMenu, menuConfig.getFileName(), menuSettings.getCommands(), menuSettings.getOpenTrigger());
	}


	private static void addIconToMenu(AdvancedIconMenu iconMenu, IconSettings iconSettings, ErrorCollector errorCollector) throws IconAddException {
		if (iconSettings.getPositionX() == null) {
			throw new IconAddException(ErrorFormat.missingAttribute(iconSettings, IconNode.POSITION_X));
		}

		if (iconSettings.getPositionY() == null) {
			throw new IconAddException(ErrorFormat.missingAttribute(iconSettings, IconNode.POSITION_Y));
		}

		int actualX = iconSettings.getPositionX().getPosition() - 1;
		int actualY = iconSettings.getPositionY().getPosition() - 1;

		if (actualX < 0 || actualX >= iconMenu.getColumnCount()) {
			throw new IconAddException(ErrorFormat.invalidAttribute(iconSettings, IconNode.POSITION_X,
					"it must be between 1 and " + iconMenu.getColumnCount()));
		}
		if (actualY < 0 || actualY >= iconMenu.getRowCount()) {
			throw new IconAddException(ErrorFormat.invalidAttribute(iconSettings, IconNode.POSITION_Y,
					"it must be between 1 and " + iconMenu.getRowCount()));
		}

		if (iconMenu.getIcon(actualX, actualY) != null) {
			throw new IconAddException(ErrorFormat.iconError(iconSettings,
					"is overriding another icon with the same position"));
		}

		if (iconSettings.getMaterialAttribute() == null) {
			errorCollector.addError(ErrorFormat.missingAttribute(iconSettings, IconNode.MATERIAL));
		}

		AdvancedIcon icon = new AdvancedIcon(Material.BEDROCK);
		iconSettings.applyAttributesTo(icon);
		iconMenu.setIcon(actualX, actualY, icon);
	}


	private static MenuSettings loadMenuSettings(Config config, ErrorCollector errorCollector) {
		ConfigSection settingsSection = config.getConfigSection(Nodes.MENU_SETTINGS);

		String title;
		try {
			title = FormatUtils.addColors(settingsSection.getRequiredString(Nodes.NAME));
			if (title.length() > 32) {
				title = title.substring(0, 32);
			}
		} catch (ConfigValueException e) {
			title = ChatColor.DARK_RED + "No name set";
			errorCollector.addError(ErrorFormat.missingMenuSetting(config.getFileName(), Nodes.NAME));
		}

		int rows;
		try {
			rows = settingsSection.getRequiredInt(Nodes.ROWS);
			if (rows <= 0) {
				rows = 1;
			}
		} catch (ConfigValueException e) {
			rows = 6; // Defaults to 6 rows
			errorCollector.addError(ErrorFormat.missingMenuSetting(config.getFileName(), Nodes.ROWS));
		}

		MenuSettings menuSettings = new MenuSettings(title, rows);

		List<String> triggeringCommands = settingsSection.getStringList(Nodes.COMMANDS);
		menuSettings.setCommands(triggeringCommands);

		List<String> serializedOpenActions = settingsSection.getStringList(Nodes.OPEN_ACTIONS);

		if (serializedOpenActions != null && !serializedOpenActions.isEmpty()) {
			List<Action> openActions = new ArrayList<>();

			for (String serializedAction : serializedOpenActions) {
				if (serializedAction != null && !serializedAction.isEmpty()) {
					openActions.add(ActionParser.parseAction(serializedAction));
				}
			}

			menuSettings.setOpenActions(openActions);
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

					menuSettings.setOpenTrigger(openTrigger);

				} catch (ParseException e) {
					errorCollector.addError(ErrorFormat.invalidMenuSetting(config.getFileName(), Nodes.OPEN_ITEM_MATERIAL, e.getMessage()));
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


	private static List<IconSettings> loadIconSettingsList(Config config, ErrorCollector errorCollector) {
		List<IconSettings> iconSettingsList = new ArrayList<>();

		for (String iconSectionName : config.getKeys(false)) {
			if (iconSectionName.equals(Nodes.MENU_SETTINGS)) {
				continue;
			}

			ConfigSection iconSection = config.getConfigSection(iconSectionName);
			IconSettings iconSettings = new IconSettings(config.getFileName(), iconSectionName);
			iconSettings.loadFrom(iconSection, errorCollector);
			iconSettingsList.add(iconSettings);
		}

		return iconSettingsList;
	}


	private static class IconAddException extends Exception {

		public IconAddException(String message) {
			super(message);
		}

	}

}
