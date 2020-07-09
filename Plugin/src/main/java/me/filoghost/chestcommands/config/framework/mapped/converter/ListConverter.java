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
package me.filoghost.chestcommands.config.framework.mapped.converter;

import me.filoghost.chestcommands.config.framework.ConfigSection;
import me.filoghost.chestcommands.util.Preconditions;

import java.lang.reflect.Type;
import java.util.List;

public class ListConverter implements Converter {

	@Override
	public void setConfigValue(ConfigSection config, String path, Object value) {
		config.set(path, value);
	}

	@Override
	public List<?> getFieldValue(ConfigSection config, String path, Type[] genericTypes) {
		Preconditions.notNull(genericTypes, "genericTypes");
		Preconditions.checkArgument(genericTypes.length == 1, "genericTypes length must be 1");

		Type listType = genericTypes[0];

		if (listType == Integer.class) {
			return config.getIntegerList(path);
		} else if (listType == String.class) {
			return config.getStringList(path);
		} else {
			throw new IllegalArgumentException("unsupported list type: " + listType);
		}
	}

	@Override
	public boolean matches(Class<?> type) {
		return type == List.class;
	}

}
