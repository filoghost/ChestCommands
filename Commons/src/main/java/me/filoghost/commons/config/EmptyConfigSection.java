/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.commons.config;

import org.bukkit.configuration.MemoryConfiguration;

public class EmptyConfigSection extends ConfigSection {

	public EmptyConfigSection() {
		super(new MemoryConfiguration());
	}

}
