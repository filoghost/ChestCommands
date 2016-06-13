package com.gmail.filoghost.chestcommands.config.yaml;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.InvalidConfigurationException;

import com.gmail.filoghost.chestcommands.util.Utils;

/**
 * A special configuration wrapper that reads the values using reflection.
 * It will also save default values if not set.
 */
public class SpecialConfig {
	
	private transient PluginConfig config;
	private transient String header;
	private transient Map<String, Object> defaultValuesMap;

	public SpecialConfig(PluginConfig config) {
		this.config = config;
	}
	
	public void setHeader(String header) {
		this.header = header;
	}
	
	public void load() throws IOException, InvalidConfigurationException, Exception {
		
		// Check if the configuration was initialized.
		if (defaultValuesMap == null) {
			defaultValuesMap = new HashMap<String, Object>();
			
			// Put the values in the default values map.
			for (Field field : getClass().getDeclaredFields()) {
				if (!isValidField(field)) continue;
						
				field.setAccessible(true);
				String configKey = formatFieldName(field);
				
				try {
					Object defaultValue = field.get(this);
					if (defaultValue != null) {
						defaultValuesMap.put(configKey, defaultValue);
					} else {
						config.getPlugin().getLogger().warning("The field " + field.getName() + " was not provided with a default value, please inform the developer.");
					}
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		
		// First of all, try to load the yaml file.
		config.load();
		
		// Save default values not set.
		boolean needsSave = false;
		for (Entry<String, Object> entry : defaultValuesMap.entrySet()) {
			if (!config.isSet(entry.getKey())) {
				needsSave = true;
				config.set(entry.getKey(), entry.getValue());
			}
		}
		
		if (needsSave) {
			config.options().header(header);
			config.save();
		}
		
		// Now read change the fields.
		for (Field field : getClass().getDeclaredFields()) {
			
			if (!isValidField(field)) continue;
			
			field.setAccessible(true);
			String configKey = formatFieldName(field);
			

			if (config.isSet(configKey)) {
				
				Class<?> type = field.getType();
					
				if (type == boolean.class || type == Boolean.class) {
					field.set(this, config.getBoolean(configKey));
					
				} else if (type == int.class || type == Integer.class) {
					field.set(this, config.getInt(configKey));
					
				} else if (type == double.class || type == Double.class) {
					field.set(this, config.getDouble(configKey));
					
				} else if (type == String.class) {
					field.set(this, Utils.addColors(config.getString(configKey))); // Always add colors.
					
				} else {
					config.getPlugin().getLogger().warning("Unknown field type: " + field.getType().getName() + " (" + field.getName() + "). Please inform the developer.");
				}
				
			} else {
				field.set(this, defaultValuesMap.get(configKey));
			}
		}		
	}

	
	private boolean isValidField(Field field) {
		int modifiers = field.getModifiers();
		return !Modifier.isTransient(modifiers) && !Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers);
	}
	
	private String formatFieldName(Field field) {
		return field.getName().replace("__", ".").replace("_", "-");
	}
	
}
