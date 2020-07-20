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
package me.filoghost.chestcommands.util.logging;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a class to collect all the errors found while loading the plugin.
 */
public abstract class ErrorCollector {

	protected final List<ErrorInfo> errors = new ArrayList<>();

	public ErrorInfo add(String message) {
		return add(message, null);
	}

	public ErrorInfo add(String message, Throwable throwable) {
		ErrorInfo errorInfo = new ErrorInfo(message);
		errorInfo.setCause(throwable);
		errors.add(errorInfo);
		return errorInfo;
	}

	public int getErrorsCount() {
		return errors.size();
	}

	public boolean hasErrors() {
		return errors.size() > 0;
	}

	public abstract void logToConsole();

}