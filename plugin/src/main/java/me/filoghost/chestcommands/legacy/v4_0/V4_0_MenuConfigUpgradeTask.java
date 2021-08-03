/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy.v4_0;

import me.filoghost.chestcommands.config.ConfigManager;
import me.filoghost.chestcommands.legacy.upgrade.YamlUpgradeTask;
import me.filoghost.chestcommands.parsing.icon.AttributeType;
import me.filoghost.chestcommands.parsing.menu.MenuSettingsPath;
import me.filoghost.fcommons.Strings;
import me.filoghost.fcommons.config.Config;
import me.filoghost.fcommons.config.ConfigPath;
import me.filoghost.fcommons.config.ConfigSection;
import me.filoghost.fcommons.config.ConfigType;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

/*
 * All the changes that are not easy to make without parsing the YML file
 */
public class V4_0_MenuConfigUpgradeTask extends YamlUpgradeTask {

    private final String legacyCommandSeparator;

    public V4_0_MenuConfigUpgradeTask(ConfigManager configManager, Path menuFile, String legacyCommandSeparator) {
        super(configManager.getConfigLoader(menuFile));
        this.legacyCommandSeparator = legacyCommandSeparator;
    }

    @Override
    public void computeYamlChanges(Config menuConfig) {
        menuConfig.setHeader();

        for (Entry<ConfigPath, ConfigSection> entry : menuConfig.toMap(ConfigType.SECTION).entrySet()) {
            ConfigPath key = entry.getKey();
            ConfigSection section = menuConfig.getConfigSection(key);
            if (section == null) {
                continue;
            }

            if (key.equals(MenuSettingsPath.ROOT_SECTION)) {
                upgradeMenuSettings(section);
            } else {
                upgradeIcon(section);
            }
        }
    }

    private void upgradeMenuSettings(ConfigSection section) {
        expandInlineList(section, MenuSettingsPath.COMMANDS, ";");
        expandInlineList(section, MenuSettingsPath.OPEN_ACTIONS, legacyCommandSeparator);
        updateActionPrefixes(section, MenuSettingsPath.OPEN_ACTIONS);
    }

    private void upgradeIcon(ConfigSection section) {
        expandInlineList(section, AttributeType.ENCHANTMENTS.getConfigKey(), ";");
        expandInlineList(section, AttributeType.ACTIONS.getConfigKey(), legacyCommandSeparator);
        updateActionPrefixes(section, AttributeType.ACTIONS.getConfigKey());
        expandSingletonList(section, AttributeType.REQUIRED_ITEMS.getConfigKey());
        expandInlineItemstack(section);
    }

    private void updateActionPrefixes(ConfigSection config, ConfigPath configPath) {
        List<String> actions = config.getStringList(configPath);
        if (actions == null) {
            return;
        }

        for (int i = 0; i < actions.size(); i++) {
            String oldAction = actions.get(i);
            String newAction = oldAction;
            newAction = replacePrefix(newAction, "menu:", "open:");
            newAction = replacePrefix(newAction, "givemoney:", "give-money:");
            newAction = replacePrefix(newAction, "dragonbar:", "dragon-bar:");
            newAction = replacePrefix(newAction, "server ", "server: ");

            if (!newAction.equals(oldAction)) {
                setSaveRequired();
                actions.set(i, newAction);
            }
        }

        config.setStringList(configPath, actions);
    }

    private String replacePrefix(String action, String oldPrefix, String newPrefix) {
        if (action.startsWith(oldPrefix)) {
            setSaveRequired();
            return newPrefix + action.substring(oldPrefix.length());
        } else {
            return action;
        }
    }

    private void expandInlineItemstack(ConfigSection section) {
        String material = section.getString(AttributeType.MATERIAL.getConfigKey());
        if (material == null) {
            return;
        }

        if (material.contains(",")) {
            String[] parts = Strings.splitAndTrim(material, ",", 2);
            if (!section.contains(AttributeType.AMOUNT.getConfigKey())) {
                try {
                    section.setInt(AttributeType.AMOUNT.getConfigKey(), Integer.parseInt(parts[1]));
                } catch (NumberFormatException e) {
                    section.setString(AttributeType.AMOUNT.getConfigKey(), parts[1]);
                }
            }
            material = parts[0];
            section.setString(AttributeType.MATERIAL.getConfigKey(), material);
            setSaveRequired();
        }

        if (material.contains(":")) {
            String[] parts = Strings.splitAndTrim(material, ":", 2);
            if (!section.contains(AttributeType.DURABILITY.getConfigKey())) {
                try {
                    section.setInt(AttributeType.DURABILITY.getConfigKey(), Integer.parseInt(parts[1]));
                } catch (NumberFormatException e) {
                    section.setString(AttributeType.DURABILITY.getConfigKey(), parts[1]);
                }
            }
            material = parts[0];
            section.setString(AttributeType.MATERIAL.getConfigKey(), material);
            setSaveRequired();
        }
    }

    private void expandInlineList(ConfigSection config, ConfigPath path, String separator) {
        if (config.get(path).isPresentAs(ConfigType.STRING)) {
            config.setStringList(path, splitListElements(config.getString(path), separator));
            setSaveRequired();
        }
    }

    private void expandSingletonList(ConfigSection config, ConfigPath path) {
        if (config.get(path).isPresentAs(ConfigType.STRING)) {
            config.setStringList(path, Collections.singletonList(config.getString(path)));
            setSaveRequired();
        }
    }

    private List<String> splitListElements(String input, String separator) {
        if (separator == null || separator.length() == 0) {
            separator = ";";
        }

        String[] splitValues = Strings.splitAndTrim(input, separator);
        List<String> values = new ArrayList<>();

        for (String value : splitValues) {
            if (!value.isEmpty()) {
                values.add(value);
            }
        }

        // Return a list with an empty value to avoid displaying the empty list value "[]" in the YML file
        if (values.isEmpty()) {
            values.add("");
        }

        return values;
    }

}
