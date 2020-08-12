/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.commons.command;

public class CommandException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CommandException(String msg) {
		super(msg);
	}

}