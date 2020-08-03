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
import me.filoghost.chestcommands.logging.ErrorMessages;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ConfigValueType<T> {

	public static final ConfigValueType<String> STRING = new ConfigValueType<>(
			(Object value) -> value instanceof String || value instanceof Number || value instanceof Boolean || value instanceof Character,
			(Object value) -> value.toString(),
			(String value) -> value,
			ErrorMessages.Config.valueNotString
	);

	public static final ConfigValueType<Boolean> BOOLEAN = new ConfigValueType<>(
			(Object value) -> value instanceof Boolean,
			(Object value) -> (Boolean) value,
			(Boolean value) -> value,
			ErrorMessages.Config.valueNotBoolean
	);

	public static final ConfigValueType<Long> LONG = newNumberType(Number::longValue);
	public static final ConfigValueType<Integer> INTEGER = newNumberType(Number::intValue);
	public static final ConfigValueType<Short> SHORT = newNumberType(Number::shortValue);
	public static final ConfigValueType<Byte> BYTE = newNumberType(Number::byteValue);
	public static final ConfigValueType<Double> DOUBLE = newNumberType(Number::doubleValue);
	public static final ConfigValueType<Float> FLOAT = newNumberType(Number::floatValue);

	public static final ConfigValueType<List<String>> STRING_LIST = newListType(STRING);
	public static final ConfigValueType<List<Integer>> INTEGER_LIST = newListType(INTEGER);

	public static final ConfigValueType<List<?>> LIST = new ConfigValueType<>(
			(Object value) -> value instanceof List,
			(Object value) -> (List<?>) value,
			(List<?> value) -> value,
			ErrorMessages.Config.valueNotList
	);

	public static final ConfigValueType<ConfigSection> CONFIG_SECTION = new ConfigValueType<>(
			(Object value) -> value instanceof ConfigurationSection,
			(Object value) -> new ConfigSection((ConfigurationSection) value),
			(ConfigSection value) -> value.getInternalYamlSection(),
			ErrorMessages.Config.valueNotSection
	);

	private final Predicate<Object> isValidConfigValueFunction;
	private final Function<Object, T> fromConfigValueFunction;
	private final Function<T, Object> toConfigValueFunction;
	private final String notConvertibleErrorMessage;

	public ConfigValueType(
			Predicate<Object> isConvertibleFunction,
			Function<Object, T> fromConfigValueFunction,
			Function<T, Object> toConfigValueFunction,
			String notConvertibleErrorMessage) {
		this.isValidConfigValueFunction = isConvertibleFunction;
		this.fromConfigValueFunction = fromConfigValueFunction;
		this.toConfigValueFunction = toConfigValueFunction;
		this.notConvertibleErrorMessage = notConvertibleErrorMessage;
	}

	protected boolean isValidConfigValue(Object rawConfigValue) {
		return rawConfigValue != null && isValidConfigValueFunction.test(rawConfigValue);
	}

	protected T fromConfigValueRequired(Object rawConfigValue) throws MissingConfigValueException, InvalidConfigValueException {
		if (rawConfigValue == null) {
			throw new MissingConfigValueException(ErrorMessages.Config.valueNotSet);
		}

		if (isValidConfigValueFunction.test(rawConfigValue)) {
			return fromConfigValueFunction.apply(rawConfigValue);
		} else {
			throw new InvalidConfigValueException(notConvertibleErrorMessage);
		}
	}

	protected T fromConfigValueOrDefault(Object rawConfigValue, T defaultValue) {
		if (rawConfigValue == null) {
			return defaultValue;
		}

		if (isValidConfigValueFunction.test(rawConfigValue)) {
			return fromConfigValueFunction.apply(rawConfigValue);
		} else {
			return defaultValue;
		}
	}

	protected Object toConfigValue(T value) {
		if (value != null) {
			return toConfigValueFunction.apply(value);
		} else {
			return null;
		}
	}


	private static <T extends Number> ConfigValueType<T> newNumberType(Function<Number, T> toTypeFunction) {
		return new ConfigValueType<>(
				(Object value) -> value instanceof Number,
				(Object value) -> toTypeFunction.apply((Number) value),
				(T value) -> value,
				ErrorMessages.Config.valueNotNumber
		);
	}

	private static <T> ConfigValueType<List<T>> newListType(ConfigValueType<T> elementType) {
		return new ConfigValueType<>(
				(Object value) -> value instanceof List,
				(Object value) -> {
					List<T> result = new ArrayList<>();

					for (Object element : ((List<?>) value)) {
						if (elementType.isValidConfigValueFunction.test(element)) {
							result.add(elementType.fromConfigValueFunction.apply(element));
						}
					}

					return result;
				},
				(List<T> value) -> value,
				ErrorMessages.Config.valueNotList
		);
	}

}
