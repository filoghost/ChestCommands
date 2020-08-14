/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.config;

import me.filoghost.commons.config.mapped.MappedConfig;
import me.filoghost.commons.config.mapped.modifier.ChatColors;

@ChatColors
public class Settings extends MappedConfig {

	public String default_color__name = "&f";
	public String default_color__lore = "&7";
	public boolean update_notifications = true;
	public int anti_click_spam_delay = 200;

	public Settings() {
		setHeader(
				"ChestCommands main configuration file.",
				"Documentation: https://filoghost.me/docs/chest-commands");
	}

}
