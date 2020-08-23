/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import me.filoghost.chestcommands.config.ConfigManager;
import me.filoghost.chestcommands.legacy.upgrade.Upgrade;
import me.filoghost.chestcommands.legacy.upgrade.UpgradeTask;
import me.filoghost.chestcommands.legacy.upgrade.UpgradeTaskException;
import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.fcommons.logging.ErrorCollector;
import me.filoghost.fcommons.logging.Log;

public class UpgradesExecutor {

    private final ConfigManager configManager;
    private boolean allUpgradesSuccessful;
    private UpgradesDoneRegistry upgradesDoneRegistry;

    public UpgradesExecutor(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public boolean run(boolean isFreshInstall, ErrorCollector errorCollector) throws UpgradeExecutorException {
        this.allUpgradesSuccessful = true;
        Path upgradesDoneFile = configManager.getRootDataFolder().resolve(".upgrades-done");

        try {
            upgradesDoneRegistry = new UpgradesDoneRegistry(upgradesDoneFile);
        } catch (IOException e) {
            // Upgrades can't proceed if metadata file is not read correctly
            throw new UpgradeExecutorException(Errors.Upgrade.metadataReadError(upgradesDoneFile), e);
        }

        if (isFreshInstall) {
            // Mark all currently existing upgrades as already done, assuming default configuration files are up to date
            upgradesDoneRegistry.setAllDone();
        } else {
            // Run missing upgrades
            Backup backup = Backup.newTimestampedBackup(configManager.getRootDataFolder());
            runMissingUpgrades(backup, errorCollector);
        }

        try {
            upgradesDoneRegistry.save();
        } catch (IOException e) {
            // Upgrades can't proceed if metadata file is not saved correctly
            throw new UpgradeExecutorException(Errors.Upgrade.metadataSaveError(upgradesDoneFile), e);
        }

        return allUpgradesSuccessful;
    }


    private void runMissingUpgrades(Backup backup, ErrorCollector errorCollector) {
        for (Upgrade upgrade : UpgradeList.getOrderedUpgrades()) {
            if (!upgradesDoneRegistry.isDone(upgrade)) {
                boolean allTasksSuccessful = tryRunUpgradeTasks(upgrade, backup, errorCollector);

                // Consider an upgrade "done" if all its tasks were completed successfully
                if (allTasksSuccessful) {
                    upgradesDoneRegistry.setDone(upgrade);
                } else {
                    allUpgradesSuccessful = false;
                }
            }
        }
    }


    private boolean tryRunUpgradeTasks(Upgrade upgrade, Backup backup, ErrorCollector errorCollector) {
        boolean allTasksSuccessful = true;

        List<UpgradeTask> upgradeTasks;
        try {
            upgradeTasks = upgrade.createUpgradeTasks(configManager);
        } catch (UpgradeTaskException e) {
            errorCollector.add(e, Errors.Upgrade.failedToPrepareUpgradeTasks);
            return false;
        }

        for (UpgradeTask upgradeTask : upgradeTasks) {
            try {
                boolean modified = upgradeTask.runAndBackupIfNecessary(backup);
                if (modified) {
                    Log.info("Automatically upgraded configuration file \"" + upgradeTask.getUpgradedFile() + "\". "
                            + "A backup of the old file has been saved.");
                }
            } catch (UpgradeTaskException e) {
                allTasksSuccessful = false;
                errorCollector.add(e, Errors.Upgrade.failedSingleUpgrade(upgradeTask.getOriginalFile()));
            }
        }

        return allTasksSuccessful;
    }

}
