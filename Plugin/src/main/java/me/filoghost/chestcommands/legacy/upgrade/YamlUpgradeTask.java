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
package me.filoghost.chestcommands.legacy.upgrade;

import me.filoghost.chestcommands.config.framework.Config;
import me.filoghost.chestcommands.config.framework.ConfigLoader;
import me.filoghost.chestcommands.config.framework.exception.ConfigLoadException;
import me.filoghost.chestcommands.config.framework.exception.ConfigSaveException;

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

	protected void removeNode(Config config, String node) {
		if (config.contains(node)) {
			config.remove(node);
			setSaveRequired();
		}
	}

}
