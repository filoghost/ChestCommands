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
package me.filoghost.chestcommands.parsing.menu;

import me.filoghost.chestcommands.action.Action;
import me.filoghost.chestcommands.config.Config;
import me.filoghost.chestcommands.config.ConfigSection;
import me.filoghost.chestcommands.config.ConfigValueException;
import me.filoghost.chestcommands.menu.InternalIconMenu;
import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.parsing.ActionParser;
import me.filoghost.chestcommands.parsing.ErrorFormat;
import me.filoghost.chestcommands.parsing.ItemStackParser;
import me.filoghost.chestcommands.parsing.ParseException;
import me.filoghost.chestcommands.parsing.icon.IconSettings;
import me.filoghost.chestcommands.parsing.icon.IconSettingsNode;
import me.filoghost.chestcommands.util.Colors;
import me.filoghost.chestcommands.util.collection.ErrorCollector;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class MenuParser {


	public static LoadedMenu loadMenu(Config menuConfig, ErrorCollector errorCollector) {
		MenuSettings menuSettings = loadMenuSettings(menuConfig, errorCollector);
		List<IconSettings> iconSettingsList = loadIconSettingsList(menuConfig, errorCollector);

		InternalIconMenu iconMenu = new InternalIconMenu(menuSettings.getTitle(), menuSettings.getRows(), menuConfig.getFileName());

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


	private static void addIconToMenu(InternalIconMenu iconMenu, IconSettings iconSettings, ErrorCollector errorCollector) throws IconAddException {
		if (iconSettings.getPositionX() == null) {
			throw new IconAddException(ErrorFormat.missingAttribute(iconSettings, IconSettingsNode.POSITION_X));
		}

		if (iconSettings.getPositionY() == null) {
			throw new IconAddException(ErrorFormat.missingAttribute(iconSettings, IconSettingsNode.POSITION_Y));
		}

		int row = iconSettings.getPositionY().getPosition() - 1;
		int column = iconSettings.getPositionX().getPosition() - 1;

		if (row < 0 || row >= iconMenu.getRowCount()) {
			throw new IconAddException(ErrorFormat.invalidAttribute(iconSettings, IconSettingsNode.POSITION_Y,
					"it must be between 1 and " + iconMenu.getRowCount()));
		}
		if (column < 0 || column >= iconMenu.getColumnCount()) {
			throw new IconAddException(ErrorFormat.invalidAttribute(iconSettings, IconSettingsNode.POSITION_X,
					"it must be between 1 and " + iconMenu.getColumnCount()));
		}

		if (iconMenu.getIcon(row, column) != null) {
			throw new IconAddException(ErrorFormat.iconError(iconSettings,
					"is overriding another icon with the same position"));
		}

		if (iconSettings.getMaterialAttribute() == null) {
			errorCollector.addError(ErrorFormat.missingAttribute(iconSettings, IconSettingsNode.MATERIAL));
		}

		InternalConfigurableIcon icon = new InternalConfigurableIcon(Material.BEDROCK);
		iconSettings.applyAttributesTo(icon);
		iconMenu.setIcon(row, column, icon);
	}


	private static MenuSettings loadMenuSettings(Config config, ErrorCollector errorCollector) {
		ConfigSection settingsSection = config.getConfigSection(MenuSettingsNode.ROOT_SECTION);

		String title;
		try {
			title = Colors.addColors(settingsSection.getRequiredString(MenuSettingsNode.NAME));
			if (title.length() > 32) {
				title = title.substring(0, 32);
			}
		} catch (ConfigValueException e) {
			title = ChatColor.DARK_RED + "No name set";
			errorCollector.addError(ErrorFormat.missingMenuSetting(config.getFileName(), MenuSettingsNode.NAME));
		}

		int rows;
		try {
			rows = settingsSection.getRequiredInt(MenuSettingsNode.ROWS);
			if (rows <= 0) {
				rows = 1;
			}
		} catch (ConfigValueException e) {
			rows = 6; // Defaults to 6 rows
			errorCollector.addError(ErrorFormat.missingMenuSetting(config.getFileName(), MenuSettingsNode.ROWS));
		}

		MenuSettings menuSettings = new MenuSettings(title, rows);

		List<String> triggeringCommands = settingsSection.getStringList(MenuSettingsNode.COMMANDS);
		menuSettings.setCommands(triggeringCommands);

		List<String> serializedOpenActions = settingsSection.getStringList(MenuSettingsNode.OPEN_ACTIONS);

		if (serializedOpenActions != null && !serializedOpenActions.isEmpty()) {
			List<Action> openActions = new ArrayList<>();

			for (String serializedAction : serializedOpenActions) {
				if (serializedAction != null && !serializedAction.isEmpty()) {
					openActions.add(ActionParser.parseAction(serializedAction));
				}
			}

			menuSettings.setOpenActions(openActions);
		}

		String openItemMaterial = settingsSection.getString(MenuSettingsNode.OPEN_ITEM_MATERIAL);
		if (openItemMaterial != null) {
			boolean leftClick = settingsSection.getBoolean(MenuSettingsNode.OPEN_ITEM_LEFT_CLICK);
			boolean rightClick = settingsSection.getBoolean(MenuSettingsNode.OPEN_ITEM_RIGHT_CLICK);

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
					errorCollector.addError(ErrorFormat.invalidMenuSetting(config.getFileName(), MenuSettingsNode.OPEN_ITEM_MATERIAL, e.getMessage()));
				}
			}
		}

		if (settingsSection.isSet(MenuSettingsNode.AUTO_REFRESH)) {
			int tenthsToRefresh = (int) (settingsSection.getDouble(MenuSettingsNode.AUTO_REFRESH) * 10.0);
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
			if (iconSectionName.equals(MenuSettingsNode.ROOT_SECTION)) {
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
