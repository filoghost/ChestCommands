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
package me.filoghost.commons.config.mapped;

import me.filoghost.commons.config.Config;
import me.filoghost.commons.config.ConfigErrors;
import me.filoghost.commons.config.ConfigLoader;
import me.filoghost.commons.config.ConfigValue;
import me.filoghost.commons.config.exception.ConfigLoadException;
import me.filoghost.commons.config.exception.ConfigSaveException;

import java.nio.file.Path;
import java.util.Map;
import java.util.function.Supplier;

public class MappedConfigLoader<T extends MappedConfig> {

	private final ConfigLoader configLoader;
	private final Supplier<T> mappedObjectConstructor;
	private Map<String, ConfigValue> defaultValues;

	public MappedConfigLoader(Path rootDataFolder, Path configPath, Supplier<T> mappedObjectConstructor) {
		this.configLoader = new ConfigLoader(rootDataFolder, configPath);
		this.mappedObjectConstructor = mappedObjectConstructor;
	}

	public T init() throws ConfigLoadException, ConfigSaveException {
		Config config = configLoader.init();
		T mappedObject = mappedObjectConstructor.get();

		ConfigMapper mapper;
		try {
			mapper = new ConfigMapper(mappedObject, config);
		} catch (ReflectiveOperationException e) {
			throw new ConfigLoadException(ConfigErrors.mapperInitError(mappedObject), e);
		}

		// Extract default values from fields
		if (defaultValues == null) {
			try {
				defaultValues = mapper.toConfigValues(mapper.getFieldValues());
			} catch (ReflectiveOperationException e) {
				throw new ConfigLoadException(ConfigErrors.fieldReadError(mappedObject), e);
			}
		}

		// Add missing values and save if necessary
		boolean modified = mapper.addMissingConfigValues(defaultValues);
		if (modified) {
			config.setHeader(mappedObject.getHeader());
			configLoader.save(config);
		}

		// Update the mapped object with the contents from the config
		try {
			mapper.injectObjectFields();
		} catch (ReflectiveOperationException e) {
			throw new ConfigLoadException(ConfigErrors.fieldInjectError(mappedObject), e);
		}
		mappedObject.postLoad();
		return mappedObject;
	}

	public Path getFile() {
		return configLoader.getFile();
	}

}
