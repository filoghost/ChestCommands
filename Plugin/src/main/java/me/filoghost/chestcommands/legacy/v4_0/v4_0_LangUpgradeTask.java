/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy.v4_0;

import me.filoghost.chestcommands.config.ConfigManager;
import me.filoghost.chestcommands.legacy.upgrade.RegexUpgradeTask;

public class v4_0_LangUpgradeTask extends RegexUpgradeTask {

	public v4_0_LangUpgradeTask(ConfigManager configManager) {
		super(configManager.getRootDataFolder().resolve("lang.yml"));
	}

	@Override
	protected void computeRegexChanges() {
		replaceString("{datavalue}", "{durability}");
	}

}
