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
package me.filoghost.chestcommands.config.framework.mapped;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MappedField {

	private final Field field;
	private final String configPath;

	public MappedField(Field field) {
		this.field = field;

		this.configPath = field.getName()
				.replace("__", ".")
				.replace("_", "-");
	}

	public Object getFromObject(MappedConfig mappedObject) throws ReflectiveOperationException {
		try {
			field.setAccessible(true);
			return field.get(mappedObject);
		} catch (Throwable t) {
			throw new ReflectiveOperationException(t);
		}
	}

	public void setToObject(MappedConfig mappedObject, Object fieldValue) throws ReflectiveOperationException {
		try {
			field.setAccessible(true);
			field.set(mappedObject, fieldValue);
		} catch (Throwable t) {
			throw new ReflectiveOperationException(t);
		}
	}

	public Type[] getGenericTypes() throws ReflectiveOperationException {
		try {
			Type genericType = field.getGenericType();
			if (genericType instanceof ParameterizedType) {
				return ((ParameterizedType) genericType).getActualTypeArguments();
			} else {
				return null;
			}
		} catch (Throwable t) {
			throw new ReflectiveOperationException(t);
		}
	}

	public List<Annotation> getAnnotations() {
		return Stream.concat(
				Arrays.stream(field.getDeclaredAnnotations()),
				Arrays.stream(field.getDeclaringClass().getDeclaredAnnotations()))
				.collect(Collectors.toList());
	}

	public String getFieldName() {
		return field.getName();
	}

	public Class<?> getFieldType() {
		return field.getType();
	}

	public String getConfigPath() {
		return configPath;
	}
}
