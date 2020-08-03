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
package me.filoghost.chestcommands.placeholder.scanner;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class PlaceholderScanner {

	private final String input;
	private final int inputLength;

	private int lastAppendIndex;
	private int placeholderStartIndex;
	private int index;
	private boolean stopExecution;

	public PlaceholderScanner(String input) {
		this.input = input;
		this.inputLength = input.length();
	}

	public boolean anyMatch(Predicate<PlaceholderMatch> predicate) {
		AtomicBoolean matchFound = new AtomicBoolean(false);

		scan(identifier -> {
			if (predicate.test(identifier)) {
				matchFound.set(true);
				stopExecution = true;
			}
		});

		return matchFound.get();
	}

	public String replace(Function<PlaceholderMatch, String> replaceFunction) {
		StringBuilder output = new StringBuilder();

		scan(identifier -> {
			String replacement = replaceFunction.apply(identifier);

			if (replacement != null) {
				// Append preceding text and replacement
				output.append(input, lastAppendIndex, placeholderStartIndex);
				output.append(replacement);
				lastAppendIndex = index + 1; // Start next append after the closing tag
			}

			// Else, if no replacement is found, ignore the placeholder replacement and proceed normally
		});

		// Append trailing text
		if (lastAppendIndex < inputLength) {
			output.append(input, lastAppendIndex, inputLength);
		}

		return output.toString();
	}

	private void scan(Consumer<PlaceholderMatch> matchCallback) {
		index = 0;
		placeholderStartIndex = 0;
		lastAppendIndex = 0;

		boolean insidePlaceholder = false;

		while (index < inputLength) {
			char currentChar = input.charAt(index);

			if (insidePlaceholder) {
				if (currentChar == '}') {
					// If the placeholder is "{player}" then the identifier is "player"
					String placeholderContent = input.substring(placeholderStartIndex + 1, index); // Skip the opening tag
					matchCallback.accept(PlaceholderMatch.parse(placeholderContent));
					if (!stopExecution) {
						return;
					}

					insidePlaceholder = false;
					placeholderStartIndex = 0;

				} else if (currentChar == '{') {
					// Nested placeholder, ignore wrapping placeholder and update placeholder start index
					placeholderStartIndex = index;
				}
			} else {
				if (currentChar == '{') {
					insidePlaceholder = true;
					placeholderStartIndex = index;
				}
			}

			index++;
		}
	}

}
