/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.commons;

import org.bukkit.ChatColor;

public final class Colors {

	public static String addColors(String input) {
		if (Strings.isEmpty(input)) {
			return input;
		}
		return ChatColor.translateAlternateColorCodes('&', input);
	}

}
