/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.config;

import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.chestcommands.placeholder.StaticPlaceholder;
import me.filoghost.commons.Colors;
import me.filoghost.commons.config.Config;
import me.filoghost.commons.config.ConfigSection;
import me.filoghost.commons.logging.ErrorCollector;

import java.util.ArrayList;
import java.util.List;

public class CustomPlaceholders {

	private final List<StaticPlaceholder> placeholders = new ArrayList<>();

	public void load(Config config, ErrorCollector errorCollector) {
		placeholders.clear();

		ConfigSection placeholdersSection = config.getConfigSection("placeholders");
		if (placeholdersSection == null) {
			return;
		}

		for (String placeholder : placeholdersSection.getKeys()) {
			String replacement = Colors.addColors(placeholdersSection.getString(placeholder));
			if (replacement == null) {
				return;
			}

			if (placeholder.length() == 0) {
				errorCollector.add(Errors.Config.emptyPlaceholder(config.getSourceFile()));
				continue;
			}

			if (placeholder.length() > 100) {
				errorCollector.add(Errors.Config.tooLongPlaceholder(config.getSourceFile(), placeholder));
				continue;
			}

			placeholders.add(new StaticPlaceholder(placeholder, replacement));
		}
	}

	public List<StaticPlaceholder> getPlaceholders() {
		return placeholders;
	}

}
