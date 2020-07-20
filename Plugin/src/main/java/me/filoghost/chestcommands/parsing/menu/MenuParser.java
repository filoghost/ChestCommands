/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.chestcommands.parsing.menu;

import me.filoghost.chestcommands.action.Action;
import me.filoghost.chestcommands.config.framework.Config;
import me.filoghost.chestcommands.config.framework.ConfigSection;
import me.filoghost.chestcommands.config.framework.exception.ConfigValueException;
import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.logging.ErrorMessages;
import me.filoghost.chestcommands.menu.InternalIconMenu;
import me.filoghost.chestcommands.parsing.ActionParser;
import me.filoghost.chestcommands.parsing.ItemStackParser;
import me.filoghost.chestcommands.parsing.ParseException;
import me.filoghost.chestcommands.parsing.icon.IconSettings;
import me.filoghost.chestcommands.parsing.icon.IconSettingsNode;
import me.filoghost.chestcommands.util.Colors;
import me.filoghost.chestcommands.util.logging.ErrorCollector;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class MenuParser {


	public static LoadedMenu loadMenu(Config menuConfig, ErrorCollector errorCollector) {
		MenuSettings menuSettings = loadMenuSettings(menuConfig, errorCollector);
		List<IconSettings> iconSettingsList = loadIconSettingsList(menuConfig, errorCollector);

		InternalIconMenu iconMenu = new InternalIconMenu(menuSettings.getTitle(), menuSettings.getRows(), menuConfig.getSourceFile());

		for (IconSettings iconSettings : iconSettingsList) {
			tryAddIconToMenu(iconMenu, iconSettings, errorCollector);
		}

		iconMenu.setRefreshTicks(menuSettings.getRefreshTicks());
		iconMenu.setOpenActions(menuSettings.getOpenActions());

		return new LoadedMenu(iconMenu, menuConfig.getSourceFile(), menuSettings.getCommands(), menuSettings.getOpenItem());
	}


	private static void tryAddIconToMenu(InternalIconMenu iconMenu, IconSettings iconSettings, ErrorCollector errorCollector) {
		if (iconSettings.getPositionX() == null) {
			errorCollector.add(ErrorMessages.Menu.missingAttribute(iconSettings, IconSettingsNode.POSITION_X));
			return;
		}

		if (iconSettings.getPositionY() == null) {
			errorCollector.add(ErrorMessages.Menu.missingAttribute(iconSettings, IconSettingsNode.POSITION_Y));
			return;
		}

		int row = iconSettings.getPositionY().getPosition() - 1;
		int column = iconSettings.getPositionX().getPosition() - 1;

		if (row < 0 || row >= iconMenu.getRowCount()) {
			errorCollector.add(ErrorMessages.Menu.invalidAttribute(iconSettings, IconSettingsNode.POSITION_Y))
					.appendMessage("it must be between 1 and " + iconMenu.getRowCount());
			return;
		}
		if (column < 0 || column >= iconMenu.getColumnCount()) {
			errorCollector.add(ErrorMessages.Menu.invalidAttribute(iconSettings, IconSettingsNode.POSITION_X))
					.appendMessage(("it must be between 1 and " + iconMenu.getColumnCount()));
			return;
		}

		if (iconMenu.getIcon(row, column) != null) {
			errorCollector.add(ErrorMessages.Menu.iconOverridesAnother(iconSettings));
		}

		if (iconSettings.getMaterialAttribute() == null) {
			errorCollector.add(ErrorMessages.Menu.missingAttribute(iconSettings, IconSettingsNode.MATERIAL));
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
			errorCollector.add(ErrorMessages.Menu.missingSetting(config.getSourceFile(), MenuSettingsNode.NAME));
		}

		int rows;
		try {
			rows = settingsSection.getRequiredInt(MenuSettingsNode.ROWS);
			if (rows <= 0) {
				rows = 1;
			}
		} catch (ConfigValueException e) {
			rows = 6; // Defaults to 6 rows
			errorCollector.add(ErrorMessages.Menu.missingSetting(config.getSourceFile(), MenuSettingsNode.ROWS));
		}

		MenuSettings menuSettings = new MenuSettings(title, rows);

		List<String> openCommands = settingsSection.getStringList(MenuSettingsNode.COMMANDS);
		menuSettings.setCommands(openCommands);

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

					MenuOpenItem openItem = new MenuOpenItem(itemReader.getMaterial(), clickType);

					if (itemReader.hasExplicitDurability()) {
						openItem.setRestrictiveDurability(itemReader.getDurability());
					}

					menuSettings.setOpenItem(openItem);

				} catch (ParseException e) {
					errorCollector.add(
							ErrorMessages.Menu.invalidSetting(config.getSourceFile(), MenuSettingsNode.OPEN_ITEM_MATERIAL),	e);
				}
			}
		}

		if (settingsSection.isSet(MenuSettingsNode.AUTO_REFRESH)) {
			int refreshTicks = (int) (settingsSection.getDouble(MenuSettingsNode.AUTO_REFRESH) * 20.0);
			if (refreshTicks < 1) {
				refreshTicks = 1;
			}
			menuSettings.setRefreshTicks(refreshTicks);
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
			IconSettings iconSettings = new IconSettings(config.getSourceFile(), iconSectionName);
			iconSettings.loadFrom(iconSection, errorCollector);
			iconSettingsList.add(iconSettings);
		}

		return iconSettingsList;
	}

}
