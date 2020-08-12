/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.commons.Colors;
import me.filoghost.commons.collection.CollectionUtils;
import org.bukkit.ChatColor;

import java.util.List;

public class LoreAttribute implements IconAttribute {

	private final List<String> lore;
	
	public LoreAttribute(List<String> lore, AttributeErrorHandler errorHandler) {
		this.lore = colorLore(lore);
	}

	private List<String> colorLore(List<String> input) {
		return CollectionUtils.transform(input, line -> {
			if (line.isEmpty()) {
				return line;
			} else if (line.charAt(0) != ChatColor.COLOR_CHAR) {
				return ChestCommands.getSettings().default_color__lore + Colors.addColors(line);
			} else {
				return Colors.addColors(line);
			}
		});
	}

	@Override
	public void apply(InternalConfigurableIcon icon) {
		icon.setLore(lore);
	}

}
