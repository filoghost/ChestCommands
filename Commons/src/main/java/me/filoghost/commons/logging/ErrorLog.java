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

import com.google.common.collect.ImmutableList;

public class ErrorLog {

	private final ImmutableList<String> message;
	private final Throwable cause;

	protected ErrorLog(Throwable cause, String[] message) {
		this.message = ImmutableList.copyOf(message);
		this.cause = cause;
	}

	public ImmutableList<String> getMessage() {
		return message;
	}

	public Throwable getCause() {
		return cause;
	}

}
