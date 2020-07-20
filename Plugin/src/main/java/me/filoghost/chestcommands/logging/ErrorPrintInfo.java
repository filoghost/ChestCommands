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
package me.filoghost.chestcommands.logging;

import java.util.List;

class ErrorPrintInfo {

	private final int index;
	private final List<String> message;
	private final String details;
	private final Throwable cause;

	public ErrorPrintInfo(int index, List<String> message, String details, Throwable cause) {
		this.index = index;
		this.message = message;
		this.details = details;
		this.cause = cause;
	}

	public int getIndex() {
		return index;
	}

	public List<String> getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}

	public Throwable getCause() {
		return cause;
	}

}
