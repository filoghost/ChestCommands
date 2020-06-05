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
package me.filoghost.chestcommands.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.configuration.ConfigurationSection;

public class ConfigUtil {
	
	public static String getAnyString(ConfigurationSection config, String... paths) {
		for (String path : paths) {
			String value = config.getString(path);
			if (value != null) {
				return value;
			}
		}
		return null;
	}
	
	public static Integer getAnyInt(ConfigurationSection config, String... paths) {
		for (String path : paths) {
			if (config.isSet(path)) {
				return config.getInt(path);
			}
		}
		return null;
	}
	
	public static List<String> getStringListOrInlineList(ConfigurationSection config, String separator, String... paths) {
		for (String path : paths) {
			if (config.isSet(path)) {
				if (config.isList(path)) {
					return config.getStringList(path);
				} else {
					return getSeparatedValues(config.getString(path), separator);
				}
			}
		}
		return null;
	}
	
	public static List<String> getStringListOrSingle(ConfigurationSection config, String... paths) {
		for (String path : paths) {
			if (config.isSet(path)) {
				if (config.isList(path)) {
					return config.getStringList(path);
				} else {
					return Collections.singletonList(config.getString(path));
				}
			}
		}
		return null;
	}
	
	public static List<String> getSeparatedValues(String input, String separator) {
		if (separator == null || separator.length() == 0) {
			separator = ";";
		}

		String[] splitValues = input.split(Pattern.quote(separator));
		List<String> values = new ArrayList<>();

		for (String value : splitValues) {
			String trimmedValue = value.trim();

			if (!trimmedValue.isEmpty()) {
				values.add(trimmedValue);
			}
		}

		return values;
	}
	

}
