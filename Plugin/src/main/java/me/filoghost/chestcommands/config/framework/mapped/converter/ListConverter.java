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
package me.filoghost.chestcommands.config.framework.mapped.converter;

import me.filoghost.chestcommands.config.framework.ConfigValueType;
import me.filoghost.chestcommands.util.Preconditions;

import java.lang.reflect.Type;
import java.util.List;

public class ListConverter implements Converter {

	@Override
	public ConfigValueType<?> getConfigValueType(Type[] fieldGenericTypes) {
		Preconditions.notNull(fieldGenericTypes, "fieldGenericTypes");
		Preconditions.checkArgument(fieldGenericTypes.length == 1, "fieldGenericTypes length must be 1");

		Type listType = fieldGenericTypes[0];

		if (listType == Integer.class) {
			return ConfigValueType.INTEGER_LIST;
		} else if (listType == String.class) {
			return ConfigValueType.STRING_LIST;
		} else {
			throw new IllegalArgumentException("unsupported list type: " + listType);
		}
	}

	@Override
	public boolean matches(Class<?> type) {
		return type == List.class;
	}

}
