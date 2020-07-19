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
package me.filoghost.chestcommands.legacy.upgrade;

import me.filoghost.chestcommands.config.ConfigManager;
import me.filoghost.chestcommands.config.framework.Config;
import me.filoghost.chestcommands.config.framework.ConfigLoader;
import me.filoghost.chestcommands.config.framework.exception.ConfigLoadException;
import me.filoghost.chestcommands.config.framework.exception.ConfigSaveException;

import java.nio.file.Path;

public class SettingsUpgrade extends Upgrade {

	private final ConfigLoader settingsConfigLoader;
	private Config updatedConfig;

	public SettingsUpgrade(ConfigManager configManager) {
		this.settingsConfigLoader = configManager.getConfigLoader("config.yml");
	}

	@Override
	public Path getOriginalFile() {
		return settingsConfigLoader.getFile();
	}

	@Override
	public Path getUpgradedFile() {
		return settingsConfigLoader.getFile();
	}

	@Override
	protected void computeChanges() throws ConfigLoadException {
		if (!settingsConfigLoader.fileExists()) {
			return;
		}
		Config settingsConfig = settingsConfigLoader.load();

		removeNode(settingsConfig, "use-only-commands-without-args");
		removeNode(settingsConfig, "use-console-colors");
		removeNode(settingsConfig, "multiple-commands-separator");

		this.updatedConfig = settingsConfig;
	}

	private void removeNode(Config config, String node) {
		if (config.isSet(node)) {
			config.set(node, null);
			setModified();
		}
	}


	@Override
	protected void saveChanges() throws ConfigSaveException {
		settingsConfigLoader.save(updatedConfig);
	}
}
