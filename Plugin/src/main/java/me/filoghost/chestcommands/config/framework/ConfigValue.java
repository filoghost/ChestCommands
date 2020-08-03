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

public class ConfigValue {

	private static final ConfigValue EMPTY = new ConfigValue(null);

	private final Object rawConfigValue;

	public static <T> ConfigValue of(ConfigValueType<T> valueType, T value) {
		Preconditions.notNull(valueType, "valueType");
		Preconditions.notNull(value, "value");
		return new ConfigValue(valueType.toConfigValue(value));
	}

	protected static ConfigValue fromRawConfigValue(Object rawConfigValue) {
		if (rawConfigValue != null) {
			return new ConfigValue(rawConfigValue);
		} else {
			return EMPTY;
		}
	}

	private ConfigValue(Object rawConfigValue) {
		this.rawConfigValue = rawConfigValue;
	}

	protected Object getRawConfigValue() {
		return rawConfigValue;
	}

	public <T> T as(ConfigValueType<T> valueType) {
		return valueType.fromConfigValueOrDefault(rawConfigValue, null);
	}

	public <T> T asRequired(ConfigValueType<T> valueType) throws MissingConfigValueException, InvalidConfigValueException {
		return valueType.fromConfigValueRequired(rawConfigValue);
	}

	public <T> T asOrDefault(ConfigValueType<T> valueType, T defaultValue) {
		return valueType.fromConfigValueOrDefault(rawConfigValue, defaultValue);
	}

	public boolean isValidAs(ConfigValueType<?> configValueType) {
		return configValueType.isValidConfigValue(rawConfigValue);
	}

}
