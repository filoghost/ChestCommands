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
package me.filoghost.chestcommands.legacy;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import me.filoghost.chestcommands.config.yaml.PluginConfig;
import me.filoghost.chestcommands.util.Strings;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class LegacyMenuConverter extends ConfigConverter {

	private static final Map<String, String> menuSettingsReplacements = ImmutableMap.<String, String>builder()
		.put("command", "commands")
		.put("open-action", "open-actions")
		.put("open-with-item.id", "open-with-item.material")
		.build();

	private static final Map<String, String> menuSettingsInlineLists = ImmutableMap.<String, String>builder()
		.put("commands", ";")
		.put("open-actions", "")
		.build();

	private static final Map<String, String> iconAttributesReplacements = ImmutableMap.<String, String>builder()
		.put("ID", "MATERIAL")
		.put("DATA-VALUE", "DURABILITY")
		.put("NBT", "NBT-DATA")
		.put("ENCHANTMENT", "ENCHANTMENTS")
		.put("COMMAND", "ACTIONS")
		.put("COMMANDS", "ACTIONS")
		.put("REQUIRED-ITEM", "REQUIRED-ITEMS")
		.build();

	private static final Map<String, String> iconAttributesInlineLists = ImmutableMap.<String, String>builder()
		.put("ACTIONS", "")
		.put("ENCHANTMENTS", ";")
		.build();

	private static final Set<String> iconAttributesSingleStringLists = ImmutableSet.of(
		"REQUIRED-ITEMS"
	);


	private final PluginConfig menuConfig;
	private final String legacyCommandSeparator;

	public LegacyMenuConverter(PluginConfig menuConfig, String legacyCommandSeparator) {
		this.menuConfig = menuConfig;
		this.legacyCommandSeparator = legacyCommandSeparator;
	}

	@Override
	protected void convert0() {
		for (String key : menuConfig.getKeys(true)) {
			if (!menuConfig.isConfigurationSection(key)) {
				continue;
			}

			ConfigurationSection section = menuConfig.getConfigurationSection(key);
			if (key.equals("menu-settings")) {
				renameNodes(section, menuSettingsReplacements);
				expandInlineLists(section, menuSettingsInlineLists);
			} else {
				renameNodes(section, iconAttributesReplacements);
				expandInlineLists(section, iconAttributesInlineLists);
				expandSingletonLists(section, iconAttributesSingleStringLists);

				convertInlineItemstack(section);
			}
		}
	}

	private void convertInlineItemstack(ConfigurationSection section) {
		String material = section.getString("MATERIAL");
		if (material == null) {
			return;
		}

		if (material.contains(",")) {
			String[] parts = Strings.trimmedSplit(material, ",", 2);
			if (!section.isSet("AMOUNT")) {
				try {
					section.set("AMOUNT", Integer.parseInt(parts[1]));
				} catch (NumberFormatException e) {
					section.set("AMOUNT", parts[1]);
				}
			}
			material = parts[0];
			section.set("MATERIAL", material);
			setModified();
		}

		if (material.contains(":")) {
			String[] parts = Strings.trimmedSplit(material, ":", 2);
			if (!section.isSet("DURABILITY")) {
				try {
					section.set("DURABILITY", Integer.parseInt(parts[1]));
				} catch (NumberFormatException e) {
					section.set("DURABILITY", parts[1]);
				}
			}
			material = parts[0];
			section.set("MATERIAL", material);
			setModified();
		}
	}

	private void renameNodes(ConfigurationSection config, Map<String, String> replacements) {
		replacements.forEach((oldNode, newNode) -> {
			if (config.isSet(oldNode) && !config.isSet(newNode)) {
				config.set(newNode, config.get(oldNode));
				config.set(oldNode, null);
				setModified();
			}
		});
	}

	private void expandInlineLists(ConfigurationSection config, Map<String, String> nodesAndSeparators) {
		nodesAndSeparators.forEach((inlineListNode, separator) -> {
			if (config.isSet(inlineListNode)) {
				if (config.isString(inlineListNode)) {
					config.set(inlineListNode, getSeparatedValues(config.getString(inlineListNode), separator));
					setModified();
				}
			}
		});
	}

	private void expandSingletonLists(ConfigurationSection config, Set<String> nodes) {
		for (String singleStringListNode : nodes) {
			if (config.isSet(singleStringListNode)) {
				config.set(singleStringListNode, Collections.singletonList(config.get(singleStringListNode)));
				setModified();
			}
		}
	}

	private List<String> getSeparatedValues(String input, String separator) {
		if (separator == null || separator.length() == 0) {
			separator = this.legacyCommandSeparator;
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
