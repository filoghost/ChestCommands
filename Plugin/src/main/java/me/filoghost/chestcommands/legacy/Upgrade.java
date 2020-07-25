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
import me.filoghost.chestcommands.config.framework.ConfigLoader;
import me.filoghost.chestcommands.legacy.upgrade.LangUpgradeTask;
import me.filoghost.chestcommands.legacy.upgrade.MenuNodeExpandUpgradeTask;
import me.filoghost.chestcommands.legacy.upgrade.MenuNodeRenameUpgradeTask;
import me.filoghost.chestcommands.legacy.upgrade.PlaceholdersYamlUpgradeTask;
import me.filoghost.chestcommands.legacy.upgrade.SettingsUpgradeTask;
import me.filoghost.chestcommands.legacy.upgrade.UpgradeTask;
import me.filoghost.chestcommands.legacy.upgrade.UpgradeTaskException;
import me.filoghost.chestcommands.logging.ErrorMessages;
import me.filoghost.chestcommands.util.collection.CollectionUtils;
import me.filoghost.chestcommands.util.logging.Log;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public enum Upgrade {

	V4_MENUS_RENAME("v4.0-menus-rename", (configManager) -> {
		List<Path> menuFiles = getMenuFiles(configManager);

		return CollectionUtils.transform(menuFiles,
				MenuNodeRenameUpgradeTask::new);
	}),

	V4_MENUS_REFORMAT("v4.0-menus-reformat", (configManager) -> {
		String legacyCommandSeparator = readLegacyCommandSeparator(configManager);
		List<Path> menuFiles = getMenuFiles(configManager);

		return CollectionUtils.transform(menuFiles,
				file -> new MenuNodeExpandUpgradeTask(configManager, file, legacyCommandSeparator));
	}),

	V4_CONFIG("v4.0-config", (configManager) -> {
		return Collections.singletonList(new SettingsUpgradeTask(configManager));
	}),

	V4_PLACEHOLDERS("v4.0-placeholders", (configManager) -> {
		return Collections.singletonList(new PlaceholdersYamlUpgradeTask(configManager));
	}),

	V4_LANG("v4.0-lang", (configManager) -> {
		return Collections.singletonList(new LangUpgradeTask(configManager));
	});


	private final String id;
	private final UpgradeTasksSupplier upgradeTasksSupplier;

	Upgrade(String id, UpgradeTasksSupplier upgradeTasksSupplier) {
		this.id = id;
		this.upgradeTasksSupplier = upgradeTasksSupplier;
	}

	public String getID() {
		return id;
	}

	public List<UpgradeTask> createUpgradeTasks(ConfigManager configManager) throws UpgradeTaskException {
		return upgradeTasksSupplier.getTasks(configManager);
	}

	private static List<Path> getMenuFiles(ConfigManager configManager) throws UpgradeTaskException {
		try {
			return configManager.getMenuFiles();
		} catch (IOException e) {
			throw new UpgradeTaskException(ErrorMessages.Upgrade.menuListIOException, e);
		}
	}
	
	private static String readLegacyCommandSeparator(ConfigManager configManager) {
		ConfigLoader settingsConfigLoader = configManager.getConfigLoader("config.yml");

		if (!settingsConfigLoader.fileExists()) {
			return null;
		}

		try {
			return settingsConfigLoader.load().getString("multiple-commands-separator");
		} catch (Throwable t) {
			Log.warning("Failed to load \"" + settingsConfigLoader.getFile() + "\", assuming default command separator \";\".");
			return null;
		}
	}

	@FunctionalInterface
	interface UpgradeTasksSupplier {

		List<UpgradeTask> getTasks(ConfigManager configManager) throws UpgradeTaskException;

	}

}
