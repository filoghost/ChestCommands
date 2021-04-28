/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy.v4_0;

import me.filoghost.chestcommands.legacy.upgrade.RegexUpgradeTask;
import me.filoghost.chestcommands.parsing.icon.AttributeType;

import java.nio.file.Path;

public class V4_0_MenuNodeRenameUpgradeTask extends RegexUpgradeTask {

    public V4_0_MenuNodeRenameUpgradeTask(Path menuFile) {
        super(menuFile);
    }

    @Override
    protected void computeRegexChanges() {
        replaceSubNode("command", "commands");
        replaceSubNode("open-action", "open-actions");
        replaceSubNode("id", "material");

        replaceOldAttribute("ID", AttributeType.MATERIAL);
        replaceOldAttribute("DATA-VALUE", AttributeType.DURABILITY);
        replaceOldAttribute("NBT", AttributeType.NBT_DATA);
        replaceOldAttribute("ENCHANTMENT", AttributeType.ENCHANTMENTS);
        replaceOldAttribute("COMMAND", AttributeType.ACTIONS);
        replaceOldAttribute("COMMANDS", AttributeType.ACTIONS);
        replaceOldAttribute("REQUIRED-ITEM", AttributeType.REQUIRED_ITEMS);
    }

    private void replaceOldAttribute(String oldNode, AttributeType newAttribute) {
        replaceSubNode(oldNode, newAttribute.getConfigKey().asRawKey());
    }

}
