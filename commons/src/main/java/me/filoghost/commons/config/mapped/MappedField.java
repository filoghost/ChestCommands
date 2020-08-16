/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.commons.config.mapped;

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
	private final Type[] genericTypes;
	private final List<Annotation> annotations;


	public MappedField(Field field) throws ReflectiveOperationException {
		this.field = field;

		this.configPath = field.getName()
				.replace("__", ".")
				.replace("_", "-");

		try {
			Type genericType = field.getGenericType();
			if (genericType instanceof ParameterizedType) {
				this.genericTypes = ((ParameterizedType) genericType).getActualTypeArguments();
			} else {
				this.genericTypes = null;
			}

			annotations = Stream.concat(
					Arrays.stream(field.getDeclaredAnnotations()),
					Arrays.stream(field.getDeclaringClass().getDeclaredAnnotations()))
					.collect(Collectors.toList());
		} catch (Throwable t) {
			throw new ReflectiveOperationException(t);
		}
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

	public Type[] getGenericTypes() {
		return genericTypes;
	}

	public List<Annotation> getAnnotations() {
		return annotations;
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
