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
package me.filoghost.chestcommands.config.framework;

import me.filoghost.chestcommands.config.framework.mapped.MappedConfig;
import me.filoghost.chestcommands.config.framework.mapped.MappedConfigLoader;

import java.nio.file.Path;
import java.util.function.Supplier;

public class BaseConfigManager {

	protected final Path rootDataFolder;

	public BaseConfigManager(Path rootDataFolder) {
		this.rootDataFolder = rootDataFolder;
	}

	public Path getRootDataFolder() {
		return rootDataFolder;
	}

	public ConfigLoader getConfigLoader(String fileName) {
		return getConfigLoader(rootDataFolder.resolve(fileName));
	}

	public ConfigLoader getConfigLoader(Path configPath) {
		return new ConfigLoader(rootDataFolder, configPath);
	}

	public <T extends MappedConfig> MappedConfigLoader<T> getMappedConfigLoader(String fileName, Supplier<T> mappedObjectConstructor) {
		return getMappedConfigLoader(rootDataFolder.resolve(fileName), mappedObjectConstructor);
	}

	public <T extends MappedConfig> MappedConfigLoader<T> getMappedConfigLoader(Path configPath, Supplier<T> mappedObjectConstructor) {
		return new MappedConfigLoader<>(rootDataFolder, configPath, mappedObjectConstructor);
	}

}
