/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.commons.config.mapped.modifier;

import me.filoghost.commons.Colors;

public class ChatColorsModifier implements ValueModifier<String, ChatColors> {

	@Override
	public String transformChecked(ChatColors annotation, String value) {
		return Colors.addColors(value);
	}

	@Override
	public Class<ChatColors> getAnnotationType() {
		return ChatColors.class;
	}

	@Override
	public Class<String> getValueType() {
		return String.class;
	}

}
