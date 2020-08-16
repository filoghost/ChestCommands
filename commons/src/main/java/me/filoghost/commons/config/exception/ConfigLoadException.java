/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.commons.config.exception;

public class ConfigLoadException extends ConfigException {

	public ConfigLoadException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigLoadException(String message) {
		super(message);
	}

}
