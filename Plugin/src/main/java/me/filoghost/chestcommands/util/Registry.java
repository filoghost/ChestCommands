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
package me.filoghost.chestcommands.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Registry<T> {
	
	// Characters to ignore when searching enums by name
	private static final char[] KEY_IGNORE_CHARS = {'-', '_', ' '};
	
	private final Class<T> valuesType;
	private final Map<String, T> valuesMap;
	
	

	public static <T extends Enum<T>> Registry<T> fromEnumValues(Class<T> enumClass) {
		Registry<T> registry = new Registry<>(enumClass);
		registry.putAll(enumClass.getEnumConstants(), Enum::name);
		return registry;
	}
	
	
	public static <T> Registry<T> fromValues(T[] values, Function<T, String> toKeyFunction) {
		Registry<T> registry = new Registry<>(null);
		registry.putAll(values, toKeyFunction);
		return registry;
	}
	
	
	private Registry(Class<T> valuesType) {
		this.valuesType = valuesType;
		this.valuesMap = new HashMap<>();
	}
	
	public T find(String key) {
		if (key == null) {
			return null;
		}
		return valuesMap.get(toKeyFormat(key));
	}
	
	public void putIfEnumExists(String key, String enumValueName) {
		Preconditions.checkState(valuesType.isEnum(), "value type is not an enum");
		
		try {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			T enumValue = (T) Enum.valueOf((Class<Enum>) valuesType, enumValueName);
			put(key, enumValue);
		} catch (IllegalArgumentException e) {
			// Ignore, enum value doesn't exist
		}
	}
	
	private void putAll(T[] enumValues, Function<T, String> toKeyFunction) {
		for (T enumValue : enumValues) {
			valuesMap.put(toKeyFormat(toKeyFunction.apply(enumValue)), enumValue);
		}
	}
	
	public void put(String key, T enumValue) {
		valuesMap.put(toKeyFormat(key), enumValue);
	}
	
	private String toKeyFormat(String enumValueName) {
		return StringUtils.stripChars(enumValueName, KEY_IGNORE_CHARS).toLowerCase();
	}

	@Override
	public String toString() {
		return "Registry [type=" + valuesType + ", values=" + valuesMap + "]";
	}

}
