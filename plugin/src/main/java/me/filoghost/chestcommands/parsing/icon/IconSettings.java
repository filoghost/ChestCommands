/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.parsing.icon;

import me.filoghost.chestcommands.attribute.AttributeErrorHandler;
import me.filoghost.chestcommands.attribute.IconAttribute;
import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.chestcommands.parsing.ParseException;
import me.filoghost.fcommons.config.ConfigSection;
import me.filoghost.fcommons.config.ConfigValue;
import me.filoghost.fcommons.config.exception.ConfigValueException;
import me.filoghost.fcommons.logging.ErrorCollector;
import org.bukkit.Material;

import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

public class IconSettings {

	private final Path menuFile;
	private final String iconName;
	private final Map<AttributeType, IconAttribute> attributes;

	public IconSettings(Path menuFile, String iconName) {
		this.menuFile = menuFile;
		this.iconName = iconName;
		this.attributes = new EnumMap<>(AttributeType.class);
	}

	public InternalConfigurableIcon createIcon() {
		InternalConfigurableIcon icon = new InternalConfigurableIcon(Material.BEDROCK);

		for (IconAttribute attribute : attributes.values()) {
			attribute.apply(icon);
		}

		return icon;
	}

	public IconAttribute getAttributeValue(AttributeType attributeType) {
		return attributes.get(attributeType);
	}

	public void loadFrom(ConfigSection config, ErrorCollector errorCollector) {
		for (String attributeName : config.getKeys()) {
			try {
				AttributeType attributeType = AttributeType.fromAttributeName(attributeName);
				if (attributeType == null) {
					throw new ParseException(Errors.Parsing.unknownAttribute);
				}

				AttributeErrorHandler errorHandler = (String listElement, ParseException e) -> {
					errorCollector.add(e, Errors.Menu.invalidAttributeListElement(this, attributeName, listElement));
				};

				ConfigValue configValue = config.get(attributeName);
				IconAttribute iconAttribute = attributeType.getParser().parse(configValue, errorHandler);
				attributes.put(attributeType, iconAttribute);

			} catch (ParseException | ConfigValueException e) {
				errorCollector.add(e, Errors.Menu.invalidAttribute(this, attributeName));
			}
		}
	}

	public Path getMenuFile() {
		return menuFile;
	}

	public String getIconName() {
		return iconName;
	}

}
