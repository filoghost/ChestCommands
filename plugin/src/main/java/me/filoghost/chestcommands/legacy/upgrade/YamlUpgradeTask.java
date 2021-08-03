/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy.upgrade;

import me.filoghost.fcommons.config.Config;
import me.filoghost.fcommons.config.ConfigLoader;
import me.filoghost.fcommons.config.exception.ConfigLoadException;
import me.filoghost.fcommons.config.exception.ConfigSaveException;

import java.nio.file.Path;

public abstract class YamlUpgradeTask extends UpgradeTask {

    private final ConfigLoader configLoader;
    private Config updatedConfig;

    public YamlUpgradeTask(ConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    @Override
    public final Path getOriginalFile() {
        return configLoader.getFile();
    }

    @Override
    public final Path getUpgradedFile() {
        return configLoader.getFile();
    }

    @Override
    public final void computeChanges() throws ConfigLoadException {
        if (!configLoader.fileExists()) {
            return;
        }
        Config config = configLoader.load();
        computeYamlChanges(config);
        this.updatedConfig = config;
    }

    @Override
    public final void saveChanges() throws ConfigSaveException {
        configLoader.save(updatedConfig);
    }

    protected abstract void computeYamlChanges(Config config);

    protected void removeValue(Config config, String configPath) {
        if (config.contains(configPath)) {
            config.remove(configPath);
            setSaveRequired();
        }
    }

    protected void replaceStringValue(Config settingsConfig, String configPath, String target, String replacement) {
        String value = settingsConfig.getString(configPath);
        if (value.contains(target)) {
            settingsConfig.setString(configPath, value.replace(target, replacement));
            setSaveRequired();
        }
    }

}
