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

import me.filoghost.chestcommands.util.Strings;

import java.util.List;

class MessagePartJoiner {

	private final StringBuilder output;

	private String previousMessagePart;
	private boolean appendedFirstSentenceSeparator;

	public static String join(List<String> messageParts) {
		int estimateLength = getEstimateLength(messageParts);
		MessagePartJoiner errorMessageBuilder = new MessagePartJoiner(estimateLength);
		for (String messagePart : messageParts) {
			errorMessageBuilder.append(messagePart);
		}
		return errorMessageBuilder.build();
	}

	private static int getEstimateLength(List<String> messageParts) {
		int estimateLength = 0;

		// Length of message parts
		for (String messagePart : messageParts) {
			estimateLength += messagePart.length();
		}

		// Length of separators in between
		estimateLength += (messageParts.size() - 1) * 2;

		return estimateLength;
	}

	private MessagePartJoiner(int estimateLength) {
		output = new StringBuilder(estimateLength);
	}

	private void append(String messagePart) {
		appendSeparator();
		appendMessagePart(messagePart);

		previousMessagePart = messagePart;
	}

	private void appendMessagePart(String messagePart) {
		if (previousMessagePart == null || previousMessagePart.endsWith(".")) {
			output.append(Strings.capitalizeFirst(messagePart));
		} else {
			output.append(messagePart);
		}
	}

	private void appendSeparator() {
		if (previousMessagePart != null) {
			if (previousMessagePart.endsWith(".")) {
				output.append(" ");
				this.appendedFirstSentenceSeparator = false;

			} else if (!appendedFirstSentenceSeparator) {
				output.append(": ");
				this.appendedFirstSentenceSeparator = true;

			} else {
				output.append(", ");
			}
		}
	}

	private String build() {
		return output.toString();
	}

}
