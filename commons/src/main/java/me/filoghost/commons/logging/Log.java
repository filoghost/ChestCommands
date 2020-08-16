/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.commons.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {

	private static Logger logger;
	
	public static void setLogger(Logger logger) {
		Log.logger = logger;
	}

	public static void info(String msg) {
		info(msg, null);
	}
	
	public static void info(String msg, Throwable thrown) {
		logger.log(Level.INFO, msg, thrown);
	}

	public static void warning(String msg) {
		warning(msg, null);
	}

	public static void warning(String msg, Throwable thrown) {
		logger.log(Level.WARNING, msg, thrown);
	}

	public static void severe(String msg) {
		severe(msg, null);
	}

	public static void severe(String msg, Throwable thrown) {
		logger.log(Level.SEVERE, msg, thrown);
	}
	
}
