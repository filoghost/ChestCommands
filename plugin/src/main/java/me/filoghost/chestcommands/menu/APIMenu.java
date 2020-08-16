/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.menu;

import me.filoghost.commons.Preconditions;
import org.bukkit.plugin.Plugin;

public class APIMenu extends BaseMenu {

	private final Plugin owner;
	
	public APIMenu(Plugin owner, String title, int rows) {
		super(title, rows);
		Preconditions.notNull(owner, "owner");
		this.owner = owner;
	}

	public Plugin getOwner() {
		return owner;
	}

}
