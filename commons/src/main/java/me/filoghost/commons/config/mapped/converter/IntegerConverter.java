/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.commons.config.mapped.converter;

import me.filoghost.commons.config.ConfigValueType;

import java.lang.reflect.Type;

public class IntegerConverter implements Converter {

	@Override
	public ConfigValueType<?> getConfigValueType(Type[] fieldGenericTypes) {
		return ConfigValueType.INTEGER;
	}

	@Override
	public boolean matches(Class<?> type) {
		return type == Integer.class || type == int.class;
	}

}
