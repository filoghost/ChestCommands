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

import me.filoghost.chestcommands.util.FormatUtils;
import me.filoghost.chestcommands.util.Log;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A special configuration wrapper that reads the values using reflection.
 * It will also save default values if not set.
 */
public abstract class SpecialConfig {

	private transient String header;
	private transient Map<String, Object> defaultValuesMap;

	public void setHeader(String header) {
		this.header = header;
	}

	public void load(ConfigLoader loader) throws IOException, IllegalAccessException, InvalidConfigurationException {
		Config config = loader.load();

		// Check if the configuration was initialized
		if (defaultValuesMap == null) {
			defaultValuesMap = new HashMap<>();

			// Put the values in the default values map
			for (Field field : getClass().getDeclaredFields()) {
				if (skipField(field)) continue;

				field.setAccessible(true);
				String configKey = getConfigNode(field);

				try {
					Object defaultValue = field.get(this);
					if (defaultValue != null) {
						defaultValuesMap.put(configKey, defaultValue);
					} else {
						Log.warning("The field " + field.getName() + " was not provided with a default value, please inform the developer.");
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		boolean needsSave = false;

		// Save default values not set
		for (Entry<String, Object> entry : defaultValuesMap.entrySet()) {
			if (!config.isSet(entry.getKey())) {
				needsSave = true;
				config.set(entry.getKey(), entry.getValue());
			}
		}

		if (needsSave) {
			config.setHeader(header);
			loader.save(config);
		}

		// Now read change the fields
		for (Field field : getClass().getDeclaredFields()) {
			if (skipField(field)) {
				continue;
			}

			field.setAccessible(true);
			String configNode = getConfigNode(field);

			if (config.isSet(configNode)) {
				Class<?> type = field.getType();

				if (type == boolean.class || type == Boolean.class) {
					field.set(this, config.getBoolean(configNode));

				} else if (type == int.class || type == Integer.class) {
					field.set(this, config.getInt(configNode));

				} else if (type == double.class || type == Double.class) {
					field.set(this, config.getDouble(configNode));

				} else if (type == String.class) {
					field.set(this, FormatUtils.addColors(config.getString(configNode))); // Always add colors

				} else {
					Log.warning("Unknown field type: " + field.getType().getName() + " (" + field.getName() + "). Please inform the developer.");
				}

			} else {
				field.set(this, defaultValuesMap.get(configNode));
			}
		}
	}


	private boolean skipField(Field field) {
		int modifiers = field.getModifiers();
		return Modifier.isTransient(modifiers) || Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers);
	}

	private String getConfigNode(Field field) {
		return field.getName().replace("__", ".").replace("_", "-");
	}

}
