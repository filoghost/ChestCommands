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
package me.filoghost.commons.config.mapped.converter;

import me.filoghost.commons.config.ConfigErrors;
import me.filoghost.commons.config.ConfigValue;
import me.filoghost.commons.config.ConfigValueType;
import me.filoghost.commons.config.exception.ConverterCastException;
import me.filoghost.commons.config.mapped.MappedField;

import java.lang.reflect.Type;

public interface Converter {

	ConfigValueType<?> getConfigValueType(Type[] fieldGenericTypes);

	boolean matches(Class<?> type);

	default ConfigValue toConfigValue(MappedField mappedField, Object value) throws ConverterCastException {
		ConfigValueType<?> configValueType = getConfigValueType(mappedField.getGenericTypes());

		// Assume that ConfigValueType is compatible with the value
		return toTypedConfigValue(configValueType, value);
	}

	@SuppressWarnings("unchecked")
	default <T> ConfigValue toTypedConfigValue(ConfigValueType<T> configValueType, Object value) throws ConverterCastException {
		try {
			return ConfigValue.of(configValueType, (T) value);
		} catch (ClassCastException e) {
			throw new ConverterCastException(ConfigErrors.converterFailed(value, this), e);
		}
	}

}