/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;

public class ClickPermissionMessageAttribute implements IconAttribute {

	private final String clickPermissionMessage;

	public ClickPermissionMessageAttribute(String clickPermissionMessage, AttributeErrorHandler errorHandler) {
		this.clickPermissionMessage = clickPermissionMessage;
	}
	
	@Override
	public void apply(InternalConfigurableIcon icon) {
		icon.setNoClickPermissionMessage(clickPermissionMessage);
	}

}
