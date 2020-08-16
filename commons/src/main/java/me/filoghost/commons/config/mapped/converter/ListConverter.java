/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.commons.config.mapped.converter;

import me.filoghost.commons.Preconditions;
import me.filoghost.commons.config.ConfigValueType;

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
