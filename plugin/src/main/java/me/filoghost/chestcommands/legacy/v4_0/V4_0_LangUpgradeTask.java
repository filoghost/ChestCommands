/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy.v4_0;

import me.filoghost.chestcommands.config.ConfigManager;
import me.filoghost.chestcommands.legacy.upgrade.YamlUpgradeTask;
import me.filoghost.fcommons.config.Config;

public class V4_0_LangUpgradeTask extends YamlUpgradeTask {

    public V4_0_LangUpgradeTask(ConfigManager configManager) {
        super(configManager.getConfigLoader("lang.yml"));
    }

    @Override
    public void computeYamlChanges(Config settingsConfig) {
        removeValue(settingsConfig, "open-menu");
        removeValue(settingsConfig, "open-menu-others");
        replaceStringValue(settingsConfig, "no-required-item", "{datavalue}", "{durability}");
    }

}
