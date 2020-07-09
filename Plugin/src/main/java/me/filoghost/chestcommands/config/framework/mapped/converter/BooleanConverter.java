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

import java.lang.reflect.Type;

public class BooleanConverter implements Converter {

	@Override
	public void setConfigValue(ConfigSection config, String path, Object value) {
		config.set(path, value);
	}

	@Override
	public Boolean getFieldValue(ConfigSection config, String path, Type[] parameterizedTypes) {
		return config.getBoolean(path);
	}

	@Override
	public boolean matches(Class<?> type) {
		return type == Boolean.class || type == boolean.class;
	}

}
