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
package me.filoghost.chestcommands.config.framework;

import me.filoghost.chestcommands.config.framework.exception.InvalidConfigValueException;
import me.filoghost.chestcommands.config.framework.exception.MissingConfigValueException;
import me.filoghost.chestcommands.util.Preconditions;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Set;

public class ConfigSection {

	private final ConfigurationSection yamlSection;

	public ConfigSection(ConfigurationSection yamlSection) {
		Preconditions.notNull(yamlSection, "yamlSection");
		this.yamlSection = yamlSection;
	}

	public ConfigValue get(String path) {
		return ConfigValue.fromRawConfigValue(getRawValue(path));
	}

	public <T> T get(String path, ConfigValueType<T> configValueType) {
		return getOrDefault(path, configValueType, null);
	}

	public <T> T getRequired(String path, ConfigValueType<T> configValueType) throws MissingConfigValueException, InvalidConfigValueException {
		return configValueType.fromConfigValueRequired(getRawValue(path));
	}

	public <T> T getOrDefault(String path, ConfigValueType<T> configValueType, T defaultValue) {
		return configValueType.fromConfigValueOrDefault(getRawValue(path), defaultValue);
	}

	public <T> void set(String path, ConfigValue configValue) {
		setRawValue(path, configValue.getRawConfigValue());
	}

	public <T> void set(String path, ConfigValueType<T> configValueType, T value) {
		setRawValue(path, configValueType.toConfigValue(value));
	}

	public boolean contains(String path) {
		return getRawValue(path) != null;
	}

	public void remove(String path) {
		setRawValue(path, null);
	}

	public ConfigSection createSection(String path) {
		return new ConfigSection(yamlSection.createSection(path));
	}

	public Set<String> getKeys() {
		return yamlSection.getKeys(false);
	}

	private Object getRawValue(String path) {
		return yamlSection.get(path, null);
	}

	private void setRawValue(String path, Object value) {
		Preconditions.checkArgument(!(value instanceof ConfigurationSection), "cannot set ConfigurationSection as value");
		yamlSection.set(path, value);
	}

	protected ConfigurationSection getInternalYamlSection() {
		return yamlSection;
	}

	/*
	 * Convenience getRequired{TYPE} alias methods
	 */

	public String getRequiredString(String path) throws MissingConfigValueException, InvalidConfigValueException {
		return getRequired(path, ConfigValueType.STRING);
	}

	public boolean getRequiredBoolean(String path) throws MissingConfigValueException, InvalidConfigValueException {
		return getRequired(path, ConfigValueType.BOOLEAN);
	}

	public int getRequiredInt(String path) throws MissingConfigValueException, InvalidConfigValueException {
		return getRequired(path, ConfigValueType.INTEGER);
	}

	public double getRequiredDouble(String path) throws MissingConfigValueException, InvalidConfigValueException {
		return getRequired(path, ConfigValueType.DOUBLE);
	}

	public List<String> getRequiredStringList(String path) throws MissingConfigValueException, InvalidConfigValueException {
		return getRequired(path, ConfigValueType.STRING_LIST);
	}

	public ConfigSection getRequiredConfigSection(String path) throws MissingConfigValueException, InvalidConfigValueException {
		return getRequired(path, ConfigValueType.CONFIG_SECTION);
	}

	/*
	 * Convenience get{TYPE} (without defaults) alias methods
	 */
	public String getString(String path) {
		return getOrDefault(path, ConfigValueType.STRING, null);
	}

	public boolean getBoolean(String path) {
		return getOrDefault(path, ConfigValueType.BOOLEAN, false);
	}

	public int getInt(String path) {
		return getOrDefault(path, ConfigValueType.INTEGER, 0);
	}

	public double getDouble(String path) {
		return getOrDefault(path, ConfigValueType.DOUBLE, 0.0);
	}

	public List<String> getStringList(String path) {
		return getOrDefault(path, ConfigValueType.STRING_LIST, null);
	}

	public ConfigSection getConfigSection(String path) {
		return getOrDefault(path, ConfigValueType.CONFIG_SECTION, null);
	}

	/*
	 * Convenience get{TYPE} (with defaults) alias methods
	 */
	public String getString(String path, String defaultValue) {
		return getOrDefault(path, ConfigValueType.STRING, defaultValue);
	}

	public boolean getBoolean(String path, boolean defaultValue) {
		return getOrDefault(path, ConfigValueType.BOOLEAN, defaultValue);
	}

	public int getInt(String path, int defaultValue) {
		return getOrDefault(path, ConfigValueType.INTEGER, defaultValue);
	}

	public double getDouble(String path, double defaultValue) {
		return getOrDefault(path, ConfigValueType.DOUBLE, defaultValue);
	}

	public List<String> getStringList(String path, List<String> defaultValue) {
		return getOrDefault(path, ConfigValueType.STRING_LIST, defaultValue);
	}

	public ConfigSection getConfigSection(String path, ConfigSection defaultValue) {
		return getOrDefault(path, ConfigValueType.CONFIG_SECTION, defaultValue);
	}


	/*
	 * Convenience set{TYPE} alias methods
	 */
	public void setString(String path, String value) {
		set(path, ConfigValueType.STRING, value);
	}

	public void setBoolean(String path, boolean value) {
		set(path, ConfigValueType.BOOLEAN, value);
	}

	public void setInt(String path, int value) {
		set(path, ConfigValueType.INTEGER, value);
	}

	public void setDouble(String path, double value) {
		set(path, ConfigValueType.DOUBLE, value);
	}

	public void setStringList(String path, List<String> value) {
		set(path, ConfigValueType.STRING_LIST, value);
	}

	public void setConfigSection(String path, ConfigSection value) {
		set(path, ConfigValueType.CONFIG_SECTION, value);
	}

}
