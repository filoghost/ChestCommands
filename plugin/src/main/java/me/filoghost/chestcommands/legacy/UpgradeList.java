/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy;

import com.google.common.collect.ImmutableList;
import me.filoghost.chestcommands.config.ConfigManager;
import me.filoghost.chestcommands.legacy.upgrade.Upgrade;
import me.filoghost.chestcommands.legacy.upgrade.UpgradeTask;
import me.filoghost.chestcommands.legacy.upgrade.UpgradeTaskException;
import me.filoghost.chestcommands.legacy.v4_0.V4_0_LangUpgradeTask;
import me.filoghost.chestcommands.legacy.v4_0.V4_0_MenuConfigUpgradeTask;
import me.filoghost.chestcommands.legacy.v4_0.V4_0_MenuRawTextFileUpgradeTask;
import me.filoghost.chestcommands.legacy.v4_0.V4_0_PlaceholdersUpgradeTask;
import me.filoghost.chestcommands.legacy.v4_0.V4_0_SettingsUpgradeTask;
import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.fcommons.collection.CollectionUtils;
import me.filoghost.fcommons.config.ConfigLoader;
import me.filoghost.fcommons.config.exception.ConfigException;
import me.filoghost.fcommons.logging.Log;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class UpgradeList {

    /*
     * Note: order of declaration determines order of execution
     */
    private static final ImmutableList<Upgrade> orderedUpgrades = ImmutableList.of(
            // Edit the raw text first
            multiTaskUpgrade("v4.0-menus-rename", (configManager) -> {
                return createMenuTasks(configManager, V4_0_MenuRawTextFileUpgradeTask::new);
            }),

            // Manipulate the configuration after editing the raw text
            multiTaskUpgrade("v4.0-menus-reformat", (configManager) -> {
                String legacyCommandSeparator = readLegacyCommandSeparator(configManager);
                return createMenuTasks(configManager,
                        file -> new V4_0_MenuConfigUpgradeTask(configManager, file, legacyCommandSeparator));
            }),

            // Upgrade config after reading the command separator for menus
            singleTaskUpgrade("v4.0-config", V4_0_SettingsUpgradeTask::new),
            singleTaskUpgrade("v4.0-placeholders", V4_0_PlaceholdersUpgradeTask::new),
            singleTaskUpgrade("v4.0-lang", V4_0_LangUpgradeTask::new)
    );

    private static Upgrade singleTaskUpgrade(String id, Upgrade.SingleTaskSupplier upgradeTaskSupplier) {
        return new Upgrade(id, configManager -> {
            return Collections.singletonList(upgradeTaskSupplier.getTask(configManager));
        });
    }

    private static Upgrade multiTaskUpgrade(String id, Upgrade.MultiTaskSupplier upgradeTasksSupplier) {
        return new Upgrade(id, upgradeTasksSupplier);
    }

    private static List<UpgradeTask> createMenuTasks(ConfigManager configManager, Function<Path, UpgradeTask> menuTaskSupplier) throws UpgradeTaskException {
        List<Path> menuFiles = getMenuFiles(configManager);
        return CollectionUtils.toArrayList(menuFiles, menuTaskSupplier);
    }

    private static List<Path> getMenuFiles(ConfigManager configManager) throws UpgradeTaskException {
        try {
            return configManager.getMenuFiles();
        } catch (IOException e) {
            throw new UpgradeTaskException(Errors.Upgrade.menuListIOException, e);
        }
    }

    private static @Nullable String readLegacyCommandSeparator(ConfigManager configManager) {
        ConfigLoader settingsConfigLoader = configManager.getConfigLoader("config.yml");

        if (!settingsConfigLoader.fileExists()) {
            return null;
        }

        try {
            return settingsConfigLoader.load().getString("multiple-commands-separator");
        } catch (ConfigException e) {
            Log.warning("Failed to load \"" + settingsConfigLoader.getFile() + "\", assuming default command separator \";\".");
            return null;
        }
    }

    public static ImmutableList<Upgrade> getOrderedUpgrades() {
        return orderedUpgrades;
    }
}
