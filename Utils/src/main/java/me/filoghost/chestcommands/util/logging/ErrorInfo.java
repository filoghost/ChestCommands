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
import java.util.Collections;
import java.util.List;

public class ErrorInfo {

	private final List<String> message;
	private Throwable cause;

	protected ErrorInfo(String messagePart) {
		this.message = new ArrayList<>();
		this.message.add(messagePart);
	}

	public ErrorInfo appendMessage(String messagePart) {
		message.add(messagePart);
		return this;
	}

	public List<String> getMessage() {
		return Collections.unmodifiableList(message);
	}

	public Throwable getCause() {
		return cause;
	}

	public void setCause(Throwable cause) {
		this.cause = cause;
	}

}
