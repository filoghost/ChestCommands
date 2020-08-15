/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy.v4_0;

import me.filoghost.chestcommands.config.ConfigManager;
import me.filoghost.chestcommands.legacy.upgrade.YamlUpgradeTask;
import me.filoghost.chestcommands.parsing.icon.AttributeType;
import me.filoghost.chestcommands.parsing.menu.MenuSettingsNode;
import me.filoghost.commons.Strings;
import me.filoghost.commons.config.Config;
import me.filoghost.commons.config.ConfigSection;
import me.filoghost.commons.config.ConfigValueType;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class v4_0_MenuNodeReformatUpgradeTask extends YamlUpgradeTask {

	private final String legacyCommandSeparator;

	public v4_0_MenuNodeReformatUpgradeTask(ConfigManager configManager, Path menuFile, String legacyCommandSeparator) {
		super(configManager.getConfigLoader(menuFile));
		this.legacyCommandSeparator = legacyCommandSeparator;
	}

	@Override
	public void computeYamlChanges(Config menuConfig) {
		menuConfig.setHeader(null);

		for (String key : menuConfig.getKeys()) {
			ConfigSection section = menuConfig.getConfigSection(key);
			if (section == null) {
				continue;
			}

			if (key.equals(MenuSettingsNode.ROOT_SECTION)) {
				upgradeMenuSettings(section);
			} else {
				upgradeIcon(section);
			}
		}
	}

	private void upgradeMenuSettings(ConfigSection section) {
		expandInlineList(section, MenuSettingsNode.COMMANDS, ";");
		expandInlineList(section, MenuSettingsNode.OPEN_ACTIONS, legacyCommandSeparator);
		updateActionPrefixes(section, MenuSettingsNode.OPEN_ACTIONS);
	}

	private void upgradeIcon(ConfigSection section) {
		expandInlineList(section, AttributeType.ENCHANTMENTS.getAttributeName(), ";");
		expandInlineList(section, AttributeType.ACTIONS.getAttributeName(), legacyCommandSeparator);
		updateActionPrefixes(section, AttributeType.ACTIONS.getAttributeName());
		expandSingletonList(section, AttributeType.REQUIRED_ITEMS.getAttributeName());
		expandInlineItemstack(section);
	}

	private void updateActionPrefixes(ConfigSection config, String node) {
		List<String> actions = config.getStringList(node);
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

		config.setStringList(node, actions);
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
		String material = section.getString(AttributeType.MATERIAL.getAttributeName());
		if (material == null) {
			return;
		}

		if (material.contains(",")) {
			String[] parts = Strings.trimmedSplit(material, ",", 2);
			if (!section.contains(AttributeType.AMOUNT.getAttributeName())) {
				try {
					section.setInt(AttributeType.AMOUNT.getAttributeName(), Integer.parseInt(parts[1]));
				} catch (NumberFormatException e) {
					section.setString(AttributeType.AMOUNT.getAttributeName(), parts[1]);
				}
			}
			material = parts[0];
			section.setString(AttributeType.MATERIAL.getAttributeName(), material);
			setSaveRequired();
		}

		if (material.contains(":")) {
			String[] parts = Strings.trimmedSplit(material, ":", 2);
			if (!section.contains(AttributeType.DURABILITY.getAttributeName())) {
				try {
					section.setInt(AttributeType.DURABILITY.getAttributeName(), Integer.parseInt(parts[1]));
				} catch (NumberFormatException e) {
					section.setString(AttributeType.DURABILITY.getAttributeName(), parts[1]);
				}
			}
			material = parts[0];
			section.setString(AttributeType.MATERIAL.getAttributeName(), material);
			setSaveRequired();
		}
	}

	private void expandInlineList(ConfigSection config, String node, String separator) {
		if (config.get(node).isValidAs(ConfigValueType.STRING)) {
			config.setStringList(node, splitListElements(config.getString(node), separator));
			setSaveRequired();
		}
	}

	private void expandSingletonList(ConfigSection config, String node) {
		if (config.get(node).isValidAs(ConfigValueType.STRING)) {
			config.setStringList(node, Collections.singletonList(config.getString(node)));
			setSaveRequired();
		}
	}

	private List<String> splitListElements(String input, String separator) {
		if (separator == null || separator.length() == 0) {
			separator = ";";
		}

		String[] splitValues = Strings.trimmedSplit(input, Pattern.quote(separator));
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
