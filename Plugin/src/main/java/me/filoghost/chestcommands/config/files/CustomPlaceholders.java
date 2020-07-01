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
package me.filoghost.chestcommands.config.files;

import me.filoghost.chestcommands.config.Config;
import me.filoghost.chestcommands.util.collection.ErrorCollector;
import me.filoghost.chestcommands.util.Colors;
import me.filoghost.chestcommands.util.collection.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CustomPlaceholders {

	private final Map<String, String> placeholders = new HashMap<>();

	public void load(Config config, ErrorCollector errorCollector) {
		placeholders.clear();

		for (String key : config.getKeys(false)) {
			String placeholder = key;
			String replacement = Colors.addColors(config.getString(key));

			if (placeholder.length() == 0) {
				errorCollector.addError("Error in " + config.getFileName() + ": placeholder cannot be empty (skipped).");
				continue;
			}

			if (placeholder.length() > 100) {
				errorCollector.addError("Error in " + config.getFileName() + ": placeholder cannot be longer than 100 character (" + placeholder + ").");
				continue;
			}

			placeholders.put(placeholder, replacement);
		}
	}

	public List<String> replaceAll(List<String> input) {
		if (input == null) {
			return null;
		}
		return CollectionUtils.transform(input, this::replaceAll);
	}

	public String replaceAll(String input) {
		if (input == null) {
			return null;
		}
		for (Entry<String, String> entry : placeholders.entrySet()) {
			input = input.replace(entry.getKey(), entry.getValue());
		}
		return input;
	}

}
