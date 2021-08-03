/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy.v4_0;

import me.filoghost.chestcommands.config.ConfigManager;
import me.filoghost.chestcommands.legacy.upgrade.YamlUpgradeTask;
import me.filoghost.fcommons.config.Config;

public class V4_0_SettingsUpgradeTask extends YamlUpgradeTask {

    public V4_0_SettingsUpgradeTask(ConfigManager configManager) {
        super(configManager.getConfigLoader("config.yml"));
    }

    @Override
    public void computeYamlChanges(Config settingsConfig) {
        removeValue(settingsConfig, "use-only-commands-without-args");
        removeValue(settingsConfig, "use-console-colors");
        removeValue(settingsConfig, "multiple-commands-separator");
    }

}
