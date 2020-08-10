/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.commons.logging;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a class to collect all the errors found while loading a plugin.
 */
public abstract class ErrorCollector {

	protected final List<ErrorLog> errors = new ArrayList<>();

	public void add(String... messageParts) {
		add(null, messageParts);
	}

	public void add(Throwable cause, String... messageParts) {
		errors.add(new ErrorLog(cause, messageParts));
	}

	public int getErrorsCount() {
		return errors.size();
	}

	public boolean hasErrors() {
		return errors.size() > 0;
	}

	public abstract void logToConsole();

}