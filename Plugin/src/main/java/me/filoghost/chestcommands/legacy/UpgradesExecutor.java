/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.chestcommands.legacy;

import me.filoghost.chestcommands.config.ConfigManager;
import me.filoghost.chestcommands.legacy.upgrade.Upgrade;
import me.filoghost.chestcommands.legacy.upgrade.UpgradeTask;
import me.filoghost.chestcommands.legacy.upgrade.UpgradeTaskException;
import me.filoghost.chestcommands.logging.ErrorMessages;
import me.filoghost.chestcommands.util.logging.ErrorCollector;
import me.filoghost.chestcommands.util.logging.Log;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

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
			throw new UpgradeExecutorException(ErrorMessages.Upgrade.metadataReadError(upgradesDoneFile), e);
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
			throw new UpgradeExecutorException(ErrorMessages.Upgrade.metadataSaveError(upgradesDoneFile), e);
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
			errorCollector.add(ErrorMessages.Upgrade.failedToPrepareUpgradeTasks, e);
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
				errorCollector.add(ErrorMessages.Upgrade.failedSingleUpgrade(upgradeTask.getOriginalFile()), e);
			}
		}

		return allTasksSuccessful;
	}

}
