/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.commons.command;

public class CommandValidate {

	public static void notNull(Object o, String msg) {
		if (o == null) {
			throw new CommandException(msg);
		}
	}

	public static void isTrue(boolean b, String msg) {
		if (!b) {
			throw new CommandException(msg);
		}
	}

	public static void minLength(Object[] array, int minLength, String msg) {
		if (array.length < minLength) {
			throw new CommandException(msg);
		}
	}

}