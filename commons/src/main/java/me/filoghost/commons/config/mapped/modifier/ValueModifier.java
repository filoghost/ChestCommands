/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.commons.config.mapped.modifier;

import me.filoghost.commons.Preconditions;

import java.lang.annotation.Annotation;

public interface ValueModifier<V, A extends Annotation> {

	V transformChecked(A annotation, V value);

	Class<A> getAnnotationType();

	Class<V> getValueType();

	default boolean isApplicable(Annotation annotation, Object value) {
		return getAnnotationType().isInstance(annotation) && getValueType().isInstance(value);
	}

	default Object transform(Annotation annotation, Object fieldValue) {
		Preconditions.checkArgument(isApplicable(annotation, fieldValue), "modifier doesn't match given types");

		return transformChecked(getAnnotationType().cast(annotation), getValueType().cast(fieldValue));
	}
}
