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
package me.filoghost.chestcommands.legacy.upgrades;

import me.filoghost.chestcommands.config.Config;
import me.filoghost.chestcommands.config.ConfigLoader;
import me.filoghost.chestcommands.legacy.Upgrade;
import me.filoghost.chestcommands.legacy.UpgradeException;
import me.filoghost.chestcommands.parser.IconParser;
import me.filoghost.chestcommands.parser.MenuParser;
import me.filoghost.chestcommands.util.Strings;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class MenuUpgrade extends Upgrade {

	private final ConfigLoader menuConfigLoader;
	private final String legacyCommandSeparator;
	private Config updatedConfig;

	public MenuUpgrade(ConfigLoader menuConfigLoader, String legacyCommandSeparator) {
		this.menuConfigLoader = menuConfigLoader;
		this.legacyCommandSeparator = legacyCommandSeparator;
	}

	@Override
	public Path getOriginalFile() {
		return menuConfigLoader.getPath();
	}

	@Override
	public Path getUpgradedFile() {
		return menuConfigLoader.getPath();
	}

	@Override
	protected void computeChanges() throws UpgradeException {
		Config menuConfig = loadConfig(menuConfigLoader);
		menuConfig.options().header(null);

		for (String key : menuConfig.getKeys(true)) {
			if (!menuConfig.isConfigurationSection(key)) {
				continue;
			}

			ConfigurationSection section = menuConfig.getConfigurationSection(key);

			if (key.equals(MenuParser.Nodes.MENU_SETTINGS)) {
				upgradeMenuSettings(section);
			} else {
				upgradeIcon(section);
			}
		}

		this.updatedConfig = menuConfig;
	}

	@Override
	protected void saveChanges() throws IOException {
		menuConfigLoader.save(updatedConfig);
	}


	private void upgradeMenuSettings(ConfigurationSection section) {
		renameNode(section, "command", MenuParser.Nodes.COMMANDS);
		renameNode(section, "open-action", MenuParser.Nodes.OPEN_ACTIONS);
		renameNode(section, "open-with-item.id", MenuParser.Nodes.OPEN_ITEM_MATERIAL);

		expandInlineList(section, MenuParser.Nodes.COMMANDS, ";");
		expandInlineList(section, MenuParser.Nodes.OPEN_ACTIONS, legacyCommandSeparator);
	}

	private void upgradeIcon(ConfigurationSection section) {
		renameNode(section, "ID", IconParser.Nodes.MATERIAL);
		renameNode(section, "DATA-VALUE", IconParser.Nodes.DURABILITY);
		renameNode(section, "NBT", IconParser.Nodes.NBT_DATA);
		renameNode(section, "ENCHANTMENT", IconParser.Nodes.ENCHANTMENTS);
		renameNode(section, "COMMAND", IconParser.Nodes.ACTIONS);
		renameNode(section, "COMMANDS", IconParser.Nodes.ACTIONS);
		renameNode(section, "REQUIRED-ITEM", IconParser.Nodes.REQUIRED_ITEMS);

		expandInlineList(section, IconParser.Nodes.ACTIONS, legacyCommandSeparator);
		expandInlineList(section, IconParser.Nodes.ENCHANTMENTS, ";");

		expandSingletonList(section, IconParser.Nodes.REQUIRED_ITEMS);

		expandInlineItemstack(section);
	}

	private void expandInlineItemstack(ConfigurationSection section) {
		String material = section.getString(IconParser.Nodes.MATERIAL);
		if (material == null) {
			return;
		}

		if (material.contains(",")) {
			String[] parts = Strings.trimmedSplit(material, ",", 2);
			if (!section.isSet(IconParser.Nodes.AMOUNT)) {
				try {
					section.set(IconParser.Nodes.AMOUNT, Integer.parseInt(parts[1]));
				} catch (NumberFormatException e) {
					section.set(IconParser.Nodes.AMOUNT, parts[1]);
				}
			}
			material = parts[0];
			section.set(IconParser.Nodes.MATERIAL, material);
			setModified();
		}

		if (material.contains(":")) {
			String[] parts = Strings.trimmedSplit(material, ":", 2);
			if (!section.isSet(IconParser.Nodes.DURABILITY)) {
				try {
					section.set(IconParser.Nodes.DURABILITY, Integer.parseInt(parts[1]));
				} catch (NumberFormatException e) {
					section.set(IconParser.Nodes.DURABILITY, parts[1]);
				}
			}
			material = parts[0];
			section.set(IconParser.Nodes.MATERIAL, material);
			setModified();
		}
	}

	private void renameNode(ConfigurationSection config, String oldNode, String newNode) {
		if (config.isSet(oldNode) && !config.isSet(newNode)) {
			config.set(newNode, config.get(oldNode));
			config.set(oldNode, null);
			setModified();
		}
	}

	private void expandInlineList(ConfigurationSection config, String node, String separator) {
		if (config.isSet(node)) {
			if (config.isString(node)) {
				config.set(node, getSeparatedValues(config.getString(node), separator));
				setModified();
			}
		}
	}

	private void expandSingletonList(ConfigurationSection config, String node) {
		if (config.isSet(node)) {
			config.set(node, Collections.singletonList(config.get(node)));
			setModified();
		}
	}

	private List<String> getSeparatedValues(String input, String separator) {
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
