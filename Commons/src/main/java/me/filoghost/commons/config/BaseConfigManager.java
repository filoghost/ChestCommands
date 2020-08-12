/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.commons.config;

import me.filoghost.commons.config.mapped.MappedConfig;
import me.filoghost.commons.config.mapped.MappedConfigLoader;

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
