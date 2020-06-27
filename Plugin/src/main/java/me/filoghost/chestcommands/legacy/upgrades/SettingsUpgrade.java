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
package me.filoghost.chestcommands.legacy.upgrades;

import com.google.common.collect.ImmutableSet;
import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.config.yaml.Config;
import me.filoghost.chestcommands.config.yaml.ConfigLoader;
import me.filoghost.chestcommands.legacy.Upgrade;
import me.filoghost.chestcommands.legacy.UpgradeException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public class SettingsUpgrade extends Upgrade {

	private final ConfigLoader settingsConfigLoader;
	private Config updatedConfig;

	public SettingsUpgrade(ConfigLoader settingsConfigLoader) {
		this.settingsConfigLoader = settingsConfigLoader;
	}

	@Override
	public Path getOriginalFile() {
		return settingsConfigLoader.getPath();
	}

	@Override
	public Path getUpgradedFile() {
		return settingsConfigLoader.getPath();
	}

	@Override
	protected void computeChanges() throws UpgradeException {
		Config settingsConfig = loadConfig(settingsConfigLoader);

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
	protected void saveChanges() throws IOException {
		settingsConfigLoader.save(updatedConfig);
	}
}
