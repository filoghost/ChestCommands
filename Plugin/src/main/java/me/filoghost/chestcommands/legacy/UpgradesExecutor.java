/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.chestcommands.legacy;

import me.filoghost.chestcommands.config.ConfigManager;
import me.filoghost.chestcommands.config.framework.ConfigLoader;
import me.filoghost.chestcommands.legacy.UpgradesDoneRegistry.UpgradeID;
import me.filoghost.chestcommands.legacy.upgrades.LangUpgrade;
import me.filoghost.chestcommands.legacy.upgrades.MenuUpgrade;
import me.filoghost.chestcommands.legacy.upgrades.PlaceholdersUpgrade;
import me.filoghost.chestcommands.legacy.upgrades.SettingsUpgrade;
import me.filoghost.chestcommands.util.Log;
import me.filoghost.chestcommands.util.collection.CollectionUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UpgradesExecutor {

	private final ConfigManager configManager;
	private List<Path> failedUpgrades;
	private UpgradesDoneRegistry upgradesDoneRegistry;

	public UpgradesExecutor(ConfigManager configManager) {
		this.configManager = configManager;
	}

	public void run(boolean isFreshInstall) throws UpgradeExecutorException {
		this.failedUpgrades = new ArrayList<>();
		Path upgradesDoneFile = configManager.getRootDataFolder().resolve(".upgrades-done");

		try {
			upgradesDoneRegistry = new UpgradesDoneRegistry(upgradesDoneFile);
		} catch (IOException e) {
			// Upgrades can't proceed if metadata file is not read correctly
			throw new UpgradeExecutorException("Couldn't read upgrades metadata file \"" + upgradesDoneFile.getFileName() + "\"", e);
		}

		if (isFreshInstall) {
			// Mark all currently existing upgrades as already done, assuming default configuration files are up to date
			upgradesDoneRegistry.setAllDone();

		} else {
			String legacyCommandSeparator;
			if (!upgradesDoneRegistry.isDone(UpgradeID.V4_MENUS)) {
				legacyCommandSeparator = readLegacyCommandSeparator();
			} else {
				legacyCommandSeparator = null;
			}

			SettingsUpgrade settingsUpgrade = new SettingsUpgrade(configManager);
			runIfNecessary(UpgradeID.V4_CONFIG, settingsUpgrade);

			PlaceholdersUpgrade placeholdersUpgrade = new PlaceholdersUpgrade(configManager);
			runIfNecessary(UpgradeID.V4_PLACEHOLDERS, placeholdersUpgrade);

			LangUpgrade langUpgrade = new LangUpgrade(configManager);
			runIfNecessary(UpgradeID.V4_LANG, langUpgrade);

			try {
				List<MenuUpgrade> menuUpgrades = CollectionUtils.transform(
						configManager.getMenuPaths(),
						menuPath -> new MenuUpgrade(configManager.getConfigLoader(menuPath), legacyCommandSeparator));
				runIfNecessary(UpgradeID.V4_MENUS, menuUpgrades);
			} catch (IOException e) {
				failedUpgrades.add(configManager.getMenusFolder());
			}
		}

		try {
			upgradesDoneRegistry.save();
		} catch (IOException e) {
			// Upgrades can't proceed if metadata file is not read correctly
			throw new UpgradeExecutorException("Couldn't save upgrades metadata file \"" + upgradesDoneFile.getFileName() + "\"", e);
		}

		// Success only if no upgrade failed
		if (!failedUpgrades.isEmpty()) {
			String failedConversionFiles = failedUpgrades.stream()
					.map(Path::toString)
					.collect(Collectors.joining(", "));
			throw new UpgradeExecutorException("Failed to automatically upgrade the following files: " + failedConversionFiles);
		}
	}

	private String readLegacyCommandSeparator() {
		ConfigLoader settingsConfigLoader = configManager.getConfigLoader("config.yml");

		if (!settingsConfigLoader.fileExists()) {
			return null;
		}

		try {
			return settingsConfigLoader.load().getString("multiple-commands-separator");
		} catch (Throwable t) {
			Log.severe("Failed to load " + settingsConfigLoader.getFileName() + ", assuming default command separator \";\".");
			return null;
		}
	}


	private void runIfNecessary(UpgradeID upgradeID, Upgrade upgradeTask) {
		runIfNecessary(upgradeID, Collections.singletonList(upgradeTask));
	}


	private void runIfNecessary(UpgradeID upgradeID, List<? extends Upgrade> upgradeTasks) {
		if (upgradesDoneRegistry.isDone(upgradeID)) {
			return;
		}

		boolean failedAnyUpgrade = false;

		for (Upgrade upgradeTask : upgradeTasks) {
			try {
				boolean modified = upgradeTask.backupAndUpgradeIfNecessary();
				if (modified) {
					Log.info(
							"Automatically upgraded configuration file \""
							+ upgradeTask.getUpgradedFile().getFileName() + "\" with newer configuration nodes. "
							+ "A backup of the old file has been saved.");
				}
			} catch (UpgradeException e) {
				failedAnyUpgrade = true;
				failedUpgrades.add(upgradeTask.getOriginalFile());
				logUpgradeException(upgradeTask, e);
			}
		}

		// Upgrade ID is considered complete only if all relative upgrades tasks are successful
		if (!failedAnyUpgrade) {
			upgradesDoneRegistry.setDone(upgradeID);
		}
	}


	private void logUpgradeException(Upgrade upgrade, UpgradeException upgradeException) {
		Log.severe(
				"Error while trying to automatically upgrade "	+ upgrade.getOriginalFile() + ": " + upgradeException.getMessage(),
				upgradeException.getCause());
	}

}
