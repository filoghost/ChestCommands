/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.commons.config.mapped;

import me.filoghost.commons.config.exception.ConfigLoadException;

public class MappedConfig {

	private String header;

	protected void setHeader(String... header) {
		this.header = String.join("\n", header) + "\n";
	}

	public String getHeader() {
		return header;
	}

	public void postLoad() throws ConfigLoadException {}

}
