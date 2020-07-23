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
package me.filoghost.chestcommands.legacy.upgrade;

import me.filoghost.chestcommands.config.ConfigManager;
import me.filoghost.chestcommands.config.framework.Config;
import me.filoghost.chestcommands.config.framework.ConfigLoader;
import me.filoghost.chestcommands.config.framework.ConfigSection;
import me.filoghost.chestcommands.config.framework.exception.ConfigLoadException;
import me.filoghost.chestcommands.config.framework.exception.ConfigSaveException;
import me.filoghost.chestcommands.parsing.icon.IconSettingsNode;
import me.filoghost.chestcommands.parsing.menu.MenuSettingsNode;
import me.filoghost.chestcommands.util.Strings;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class MenuNodeExpandUpgradeTask extends UpgradeTask {

	private final ConfigLoader menuConfigLoader;
	private final String legacyCommandSeparator;
	private Config updatedConfig;

	public MenuNodeExpandUpgradeTask(ConfigManager configManager, Path menuFile, String legacyCommandSeparator) {
		this.menuConfigLoader = configManager.getConfigLoader(menuFile);
		this.legacyCommandSeparator = legacyCommandSeparator;
	}

	@Override
	public Path getOriginalFile() {
		return menuConfigLoader.getFile();
	}

	@Override
	public Path getUpgradedFile() {
		return menuConfigLoader.getFile();
	}

	@Override
	public void computeChanges() throws ConfigLoadException {
		Config menuConfig = menuConfigLoader.load();
		menuConfig.setHeader(null);

		for (String key : menuConfig.getKeys(false)) {
			if (!menuConfig.isConfigSection(key)) {
				continue;
			}

			ConfigSection section = menuConfig.getConfigSection(key);

			if (key.equals(MenuSettingsNode.ROOT_SECTION)) {
				upgradeMenuSettings(section);
			} else {
				upgradeIcon(section);
			}
		}

		this.updatedConfig = menuConfig;
	}

	@Override
	public void saveChanges() throws ConfigSaveException {
		menuConfigLoader.save(updatedConfig);
	}


	private void upgradeMenuSettings(ConfigSection section) {
		expandInlineList(section, MenuSettingsNode.COMMANDS, ";");
		expandInlineList(section, MenuSettingsNode.OPEN_ACTIONS, legacyCommandSeparator);
	}

	private void upgradeIcon(ConfigSection section) {
		expandInlineList(section, IconSettingsNode.ACTIONS, legacyCommandSeparator);
		expandInlineList(section, IconSettingsNode.ENCHANTMENTS, ";");

		expandSingletonList(section, IconSettingsNode.REQUIRED_ITEMS);

		expandInlineItemstack(section);
	}

	private void expandInlineItemstack(ConfigSection section) {
		String material = section.getString(IconSettingsNode.MATERIAL);
		if (material == null) {
			return;
		}

		if (material.contains(",")) {
			String[] parts = Strings.trimmedSplit(material, ",", 2);
			if (!section.isSet(IconSettingsNode.AMOUNT)) {
				try {
					section.set(IconSettingsNode.AMOUNT, Integer.parseInt(parts[1]));
				} catch (NumberFormatException e) {
					section.set(IconSettingsNode.AMOUNT, parts[1]);
				}
			}
			material = parts[0];
			section.set(IconSettingsNode.MATERIAL, material);
			setSaveRequired();
		}

		if (material.contains(":")) {
			String[] parts = Strings.trimmedSplit(material, ":", 2);
			if (!section.isSet(IconSettingsNode.DURABILITY)) {
				try {
					section.set(IconSettingsNode.DURABILITY, Integer.parseInt(parts[1]));
				} catch (NumberFormatException e) {
					section.set(IconSettingsNode.DURABILITY, parts[1]);
				}
			}
			material = parts[0];
			section.set(IconSettingsNode.MATERIAL, material);
			setSaveRequired();
		}
	}

	private void expandInlineList(ConfigSection config, String node, String separator) {
		if (config.isSet(node)) {
			if (config.isString(node)) {
				config.set(node, getSeparatedValues(config.getString(node), separator));
				setSaveRequired();
			}
		}
	}

	private void expandSingletonList(ConfigSection config, String node) {
		if (config.isSet(node)) {
			config.set(node, Collections.singletonList(config.get(node)));
			setSaveRequired();
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
