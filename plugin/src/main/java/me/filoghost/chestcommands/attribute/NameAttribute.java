/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.config.Settings;
import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.commons.Colors;
import org.bukkit.ChatColor;

public class NameAttribute implements IconAttribute {

	private final String name;

	public NameAttribute(String name, AttributeErrorHandler errorHandler) {
		this.name = colorName(name);
	}

	private String colorName(String input) {
		if (input == null || input.isEmpty()) {
			return input;
		}

		if (input.charAt(0) != ChatColor.COLOR_CHAR) {
			return Settings.default_color__name + Colors.addColors(input);
		} else {
			return Colors.addColors(input);
		}
	}

	@Override
	public void apply(InternalConfigurableIcon icon) {
		icon.setName(name);
	}

}
