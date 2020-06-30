package me.filoghost.chestcommands.config;

import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConfigSection {

	private final ConfigurationSection yamlSection;

	public ConfigSection(ConfigurationSection yamlSection) {
		this.yamlSection = yamlSection;
	}

	public String getRequiredString(String path) throws ConfigValueException {
		Object value = getRequired(path);
		return value.toString();
	}

	public short getRequiredShort(String path) throws ConfigValueException {
		return (short) getRequiredInt(path);
	}

	public int getRequiredInt(String path) throws ConfigValueException {
		Number value = cast(getRequired(path), Number.class, "value is not a number");
		return value.intValue();
	}

	public double getRequiredDouble(String path) throws ConfigValueException {
		Number value = cast(getRequired(path), Number.class, "value is not a number");
		return value.doubleValue();
	}

	public boolean getRequiredBoolean(String path) throws ConfigValueException {
		Boolean value = cast(getRequired(path), Boolean.class, "value is not a boolean");
		return value;
	}

	public List<String> getRequiredStringList(String path) throws ConfigValueException {
		List<?> value = cast(getRequired(path), List.class, "value is not a list");
		List<String> result = new ArrayList<>();

		for (Object object : value) {
			if (object instanceof String || isPrimitiveWrapper(object)) {
				result.add(object.toString());
			}
		}

		return result;
	}

	private Object getRequired(String path) throws ConfigValueException {
		Object value = yamlSection.get(path, null);
		if (value == null) {
			throw new ConfigValueException("value is not set");
		}
		return value;
	}

	private <T> T cast(Object obj, Class<T> castType, String errorMessage) throws ConfigValueException {
		if (obj != null && !castType.isInstance(obj)) {
			throw new ConfigValueException(errorMessage);
		}

		return castType.cast(obj);
	}

	/*
	 * Delegate methods below
	 */

	public Set<String> getKeys(boolean deep) {
		return yamlSection.getKeys(deep);
	}

	public boolean contains(String path) {
		return yamlSection.contains(path);
	}

	public boolean isSet(String path) {
		return yamlSection.isSet(path);
	}

	public void set(String path, Object value) {
		yamlSection.set(path, value);
	}

	public String getString(String path) {
		return yamlSection.getString(path);
	}

	public String getString(String path, String def) {
		return yamlSection.getString(path, def);
	}

	public int getInt(String path) {
		return yamlSection.getInt(path);
	}

	public boolean getBoolean(String path) {
		return yamlSection.getBoolean(path);
	}

	public double getDouble(String path) {
		return yamlSection.getDouble(path);
	}

	public long getLong(String path) {
		return yamlSection.getLong(path);
	}

	public List<String> getStringList(String path) {
		return yamlSection.getStringList(path);
	}

	public List<Integer> getIntegerList(String path) {
		return yamlSection.getIntegerList(path);
	}

	public Object get(String path) {
		return yamlSection.get(path);
	}

	public ConfigSection getConfigSection(String path) {
		return new ConfigSection(yamlSection.getConfigurationSection(path));
	}

	public boolean isString(String path) {
		return yamlSection.isString(path);
	}

	public boolean isConfigSection(String path) {
		return yamlSection.isConfigurationSection(path);
	}

	private boolean isPrimitiveWrapper(Object input) {
		return input instanceof Integer || input instanceof Boolean ||
				input instanceof Character || input instanceof Byte ||
				input instanceof Short || input instanceof Double ||
				input instanceof Long || input instanceof Float;
	}
}
