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
package me.filoghost.chestcommands.config.framework.mapped;

import me.filoghost.chestcommands.config.framework.Config;
import me.filoghost.chestcommands.config.framework.ConfigLoader;
import me.filoghost.chestcommands.config.framework.exception.ConfigLoadException;
import me.filoghost.chestcommands.config.framework.exception.ConfigSaveException;
import me.filoghost.chestcommands.logging.ErrorMessages;

import java.nio.file.Path;
import java.util.Map;
import java.util.function.Supplier;

public class MappedConfigLoader<T extends MappedConfig> {

	private final ConfigLoader configLoader;
	private final Supplier<T> mappedObjectConstructor;
	private Map<MappedField, Object> defaultValues;

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
			throw new ConfigLoadException(ErrorMessages.Config.mapperInitError(mappedObject), e);
		}

		// Extract default values from fields
		if (defaultValues == null) {
			try {
				defaultValues = mapper.getFieldValues();
			} catch (ReflectiveOperationException e) {
				throw new ConfigLoadException(ErrorMessages.Config.fieldReadError(mappedObject), e);
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
			throw new ConfigLoadException(ErrorMessages.Config.fieldInjectError(mappedObject), e);
		}
		mappedObject.postLoad();
		return mappedObject;
	}

	public Path getFile() {
		return configLoader.getFile();
	}

}
