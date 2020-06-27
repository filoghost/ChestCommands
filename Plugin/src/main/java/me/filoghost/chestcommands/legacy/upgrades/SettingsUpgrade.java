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

	private static final Set<String> removedConfigNodes = ImmutableSet.of(
		"use-only-commands-without-args",
		"use-console-colors",
		"multiple-commands-separator"
	);

	private final ConfigLoader settingsConfigLoader;
	private Config updatedConfig;

	public SettingsUpgrade(ChestCommands plugin) {
		this.settingsConfigLoader = plugin.getSettingsConfigLoader();
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

		for (String removedConfigNode : removedConfigNodes) {
			if (settingsConfig.isSet(removedConfigNode)) {
				settingsConfig.set(removedConfigNode, null);
				setModified();
			}
		}

		this.updatedConfig = settingsConfig;
	}

	@Override
	protected void saveChanges() throws IOException {
		settingsConfigLoader.save(updatedConfig);
	}
}
