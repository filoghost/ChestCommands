/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.parsing.menu;

import me.filoghost.chestcommands.action.Action;
import me.filoghost.chestcommands.action.DisabledAction;
import me.filoghost.chestcommands.attribute.PositionAttribute;
import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.chestcommands.menu.InternalMenu;
import me.filoghost.chestcommands.parsing.ActionParser;
import me.filoghost.chestcommands.parsing.ItemStackParser;
import me.filoghost.chestcommands.parsing.ParseException;
import me.filoghost.chestcommands.parsing.icon.AttributeType;
import me.filoghost.chestcommands.parsing.icon.IconSettings;
import me.filoghost.fcommons.Colors;
import me.filoghost.fcommons.config.ConfigPath;
import me.filoghost.fcommons.config.ConfigSection;
import me.filoghost.fcommons.config.ConfigType;
import me.filoghost.fcommons.config.FileConfig;
import me.filoghost.fcommons.config.exception.ConfigValueException;
import me.filoghost.fcommons.config.exception.MissingConfigValueException;
import me.filoghost.fcommons.logging.ErrorCollector;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class MenuParser {


    public static LoadedMenu loadMenu(FileConfig menuConfig, ErrorCollector errorCollector) {
        MenuSettings menuSettings = loadMenuSettings(menuConfig, errorCollector);
        List<IconSettings> iconSettingsList = loadIconSettingsList(menuConfig, errorCollector);

        InternalMenu menu = new InternalMenu(menuSettings.getTitle(), menuSettings.getRows(), menuConfig.getSourceFile());

        for (IconSettings iconSettings : iconSettingsList) {
            tryAddIconToMenu(menu, iconSettings, errorCollector);
        }

        menu.setRefreshTicks(menuSettings.getRefreshTicks());
        menu.setOpenActions(menuSettings.getOpenActions());

        return new LoadedMenu(menu, menuConfig.getSourceFile(), menuSettings.getCommands(), menuSettings.getOpenItem());
    }


    private static void tryAddIconToMenu(InternalMenu menu, IconSettings iconSettings, ErrorCollector errorCollector) {
        if (iconSettings.isMissingAttribute(AttributeType.POSITION_X)) {
            errorCollector.add(Errors.Menu.missingAttribute(iconSettings, AttributeType.POSITION_X));
        }
        if (iconSettings.isMissingAttribute(AttributeType.POSITION_Y)) {
            errorCollector.add(Errors.Menu.missingAttribute(iconSettings, AttributeType.POSITION_Y));
        }
        if (iconSettings.isMissingAttribute(AttributeType.MATERIAL)) {
            errorCollector.add(Errors.Menu.missingAttribute(iconSettings, AttributeType.MATERIAL));
        }

        PositionAttribute positionX = (PositionAttribute) iconSettings.getAttributeValue(AttributeType.POSITION_X);
        PositionAttribute positionY = (PositionAttribute) iconSettings.getAttributeValue(AttributeType.POSITION_Y);

        if (positionX == null || positionY == null) {
            return;
        }

        int row = positionY.getPosition() - 1;
        int column = positionX.getPosition() - 1;

        boolean invalidPosition = false;

        if (row < 0 || row >= menu.getRows()) {
            errorCollector.add(
                    Errors.Menu.invalidAttribute(iconSettings, AttributeType.POSITION_Y),
                    "it must be between 1 and " + menu.getRows());
            invalidPosition = true;
        }
        if (column < 0 || column >= menu.getColumns()) {
            errorCollector.add(
                    Errors.Menu.invalidAttribute(iconSettings, AttributeType.POSITION_X),
                    "it must be between 1 and " + menu.getColumns());
            invalidPosition = true;
        }

        if (invalidPosition) {
            return;
        }

        if (menu.getIcon(row, column) != null) {
            errorCollector.add(Errors.Menu.iconOverridesAnother(iconSettings));
        }

        menu.setIcon(row, column, iconSettings.createIcon());
    }


    private static MenuSettings loadMenuSettings(FileConfig config, ErrorCollector errorCollector) {
        ConfigSection settingsSection = config.getConfigSection(MenuSettingsPath.ROOT_SECTION);
        if (settingsSection == null) {
            errorCollector.add(Errors.Menu.missingSettingsSection(config.getSourceFile()));
            settingsSection = new ConfigSection();
        }

        String title;
        try {
            title = Colors.addColors(settingsSection.getRequiredString(MenuSettingsPath.NAME));
            if (title.length() > 32) {
                title = title.substring(0, 32);
            }
        } catch (ConfigValueException e) {
            title = ChatColor.DARK_RED + "No name set";
            addMenuSettingError(errorCollector, config, MenuSettingsPath.NAME, e);
        }

        int rows;
        try {
            rows = settingsSection.getRequiredInt(MenuSettingsPath.ROWS);
            if (rows <= 0) {
                rows = 1;
            }
        } catch (ConfigValueException e) {
            rows = 6; // Defaults to 6 rows
            addMenuSettingError(errorCollector, config, MenuSettingsPath.ROWS, e);
        }

        MenuSettings menuSettings = new MenuSettings(title, rows);

        List<String> openCommands = settingsSection.getStringList(MenuSettingsPath.COMMANDS);
        menuSettings.setCommands(openCommands);

        List<String> serializedOpenActions = settingsSection.getStringList(MenuSettingsPath.OPEN_ACTIONS);

        if (serializedOpenActions != null) {
            List<Action> openActions = new ArrayList<>();

            for (String serializedAction : serializedOpenActions) {
                if (serializedAction != null && !serializedAction.isEmpty()) {
                    try {
                        openActions.add(ActionParser.parse(serializedAction));
                    } catch (ParseException e) {
                        errorCollector.add(e, Errors.Menu.invalidSettingListElement(
                                config.getSourceFile(), MenuSettingsPath.OPEN_ACTIONS, serializedAction));
                        openActions.add(new DisabledAction(Errors.User.configurationError(
                                "an action linked to opening this menu was not executed because it was not valid")));
                    }
                }
            }

            menuSettings.setOpenActions(openActions);
        }

        String openItemMaterial = settingsSection.getString(MenuSettingsPath.OPEN_ITEM_MATERIAL);
        if (openItemMaterial != null) {
            boolean leftClick = settingsSection.getBoolean(MenuSettingsPath.OPEN_ITEM_LEFT_CLICK);
            boolean rightClick = settingsSection.getBoolean(MenuSettingsPath.OPEN_ITEM_RIGHT_CLICK);

            if (leftClick || rightClick) {
                try {
                    ItemStackParser itemReader = new ItemStackParser(openItemMaterial, false);
                    itemReader.checkNotAir();
                    ClickType clickType = ClickType.fromOptions(leftClick, rightClick);

                    MenuOpenItem openItem = new MenuOpenItem(itemReader.getMaterial(), clickType);

                    if (itemReader.hasExplicitDurability()) {
                        openItem.setRestrictiveDurability(itemReader.getDurability());
                    }

                    menuSettings.setOpenItem(openItem);

                } catch (ParseException e) {
                    errorCollector.add(e, Errors.Menu.invalidSetting(config.getSourceFile(), MenuSettingsPath.OPEN_ITEM_MATERIAL));
                }
            }
        }

        if (settingsSection.contains(MenuSettingsPath.AUTO_REFRESH)) {
            int refreshTicks = (int) (settingsSection.getDouble(MenuSettingsPath.AUTO_REFRESH) * 20.0);
            if (refreshTicks < 1) {
                refreshTicks = 1;
            }
            menuSettings.setRefreshTicks(refreshTicks);
        }

        return menuSettings;
    }

    private static void addMenuSettingError(ErrorCollector errorCollector, FileConfig config, ConfigPath missingSetting, ConfigValueException e) {
        if (e instanceof MissingConfigValueException) {
            errorCollector.add(Errors.Menu.missingSetting(config.getSourceFile(), missingSetting));
        } else {
            errorCollector.add(e, Errors.Menu.invalidSetting(config.getSourceFile(), missingSetting));
        }
    }


    private static List<IconSettings> loadIconSettingsList(FileConfig config, ErrorCollector errorCollector) {
        List<IconSettings> iconSettingsList = new ArrayList<>();

        for (Entry<ConfigPath, ConfigSection> entry : config.toMap(ConfigType.SECTION).entrySet()) {
            ConfigPath iconSectionKey = entry.getKey();
            if (iconSectionKey.equals(MenuSettingsPath.ROOT_SECTION)) {
                continue;
            }

            ConfigSection iconSection = entry.getValue();
            IconSettings iconSettings = new IconSettings(config.getSourceFile(), iconSectionKey);
            iconSettings.loadFrom(iconSection, errorCollector);
            iconSettingsList.add(iconSettings);
        }

        return iconSettingsList;
    }

}
