/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy.v4_0;

import me.filoghost.chestcommands.legacy.upgrade.RegexUpgradeTask;
import me.filoghost.chestcommands.parsing.icon.AttributeType;

import java.nio.file.Path;

public class v4_0_MenuNodeRenameUpgradeTask extends RegexUpgradeTask {

	public v4_0_MenuNodeRenameUpgradeTask(Path menuFile) {
		super(menuFile);
	}
	@Override
	protected void computeRegexChanges() {
		replaceSubNode("command", "commands");
		replaceSubNode("open-action", "open-actions");
		replaceSubNode("id", "material");

		replaceSubNode("ID", AttributeType.MATERIAL.getAttributeName());
		replaceSubNode("DATA-VALUE", AttributeType.DURABILITY.getAttributeName());
		replaceSubNode("NBT", AttributeType.NBT_DATA.getAttributeName());
		replaceSubNode("ENCHANTMENT", AttributeType.ENCHANTMENTS.getAttributeName());
		replaceSubNode("COMMAND", AttributeType.ACTIONS.getAttributeName());
		replaceSubNode("COMMANDS", AttributeType.ACTIONS.getAttributeName());
		replaceSubNode("REQUIRED-ITEM", AttributeType.REQUIRED_ITEMS.getAttributeName());
	}

}
