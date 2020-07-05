/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.chestcommands.util;

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
