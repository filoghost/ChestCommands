/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.commons.config.exception;

public class MissingConfigValueException extends ConfigValueException {

	public MissingConfigValueException(String message) {
		super(message);
	}

}
