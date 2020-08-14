/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.config;

import me.filoghost.chestcommands.logging.ErrorMessages;
import me.filoghost.chestcommands.parsing.menu.LoadedMenu;
import me.filoghost.chestcommands.parsing.menu.MenuParser;
import me.filoghost.commons.Preconditions;
import me.filoghost.commons.config.BaseConfigManager;
import me.filoghost.commons.config.Config;
import me.filoghost.commons.config.ConfigLoader;
import me.filoghost.commons.config.exception.ConfigException;
import me.filoghost.commons.config.mapped.MappedConfigLoader;
import me.filoghost.commons.logging.ErrorCollector;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigManager extends BaseConfigManager {

	private final MappedConfigLoader<Settings> settingsConfigLoader;
	private final ConfigLoader placeholdersConfigLoader;
	private final MappedConfigLoader<Lang> langConfigLoader;

	public ConfigManager(Path rootDataFolder) {
		super(rootDataFolder);

		settingsConfigLoader = getMappedConfigLoader("config.yml", Settings::new);
		placeholdersConfigLoader = getConfigLoader("custom-placeholders.yml");
		langConfigLoader = getMappedConfigLoader("lang.yml", Lang::new);
	}

	public Settings tryLoadSettings(ErrorCollector errorCollector) {
		try {
			return settingsConfigLoader.init();
		} catch (ConfigException e) {
			logConfigInitException(errorCollector, settingsConfigLoader.getFile(), e);
			return new Settings();
		}
	}

	public Lang tryLoadLang(ErrorCollector errorCollector) {
		try {
			return langConfigLoader.init();
		} catch (ConfigException e) {
			logConfigInitException(errorCollector, langConfigLoader.getFile(), e);
			return new Lang();
		}
	}

	public CustomPlaceholders tryLoadCustomPlaceholders(ErrorCollector errorCollector) {
		CustomPlaceholders placeholders = new CustomPlaceholders();

		try {
			Config placeholdersConfig = placeholdersConfigLoader.init();
			placeholders.load(placeholdersConfig, errorCollector);
		} catch (ConfigException t) {
			logConfigInitException(errorCollector, placeholdersConfigLoader.getFile(), t);
		}

		return placeholders;
	}

	public void tryCreateDefault(ErrorCollector errorCollector, ConfigLoader configLoader) {
		try {
			configLoader.createDefault();
		} catch (ConfigException e) {
			logConfigInitException(errorCollector, configLoader.getFile(), e);
		}
	}

	public Path getMenusFolder() {
		return rootDataFolder.resolve("menu");
	}

	public List<Path> getMenuFiles() throws IOException {
		Preconditions.checkState(Files.isDirectory(getMenusFolder()), "menus folder doesn't exist");

		try (Stream<Path> paths = Files.walk(getMenusFolder(), FileVisitOption.FOLLOW_LINKS)) {
			return paths.filter(this::isYamlFile).collect(Collectors.toList());
		}
	}

	private void logConfigInitException(ErrorCollector errorCollector, Path file, ConfigException e) {
		errorCollector.add(e, ErrorMessages.Config.initException(file));
	}

	public List<LoadedMenu> tryLoadMenus(ErrorCollector errorCollector) {
		List<LoadedMenu> loadedMenus = new ArrayList<>();
		List<Path> menuFiles;

		try {
			menuFiles = getMenuFiles();
		} catch (IOException e) {
			errorCollector.add(e, ErrorMessages.Config.menuListIOException(getMenusFolder()));
			return Collections.emptyList();
		}

		for (Path menuFile : menuFiles) {
			ConfigLoader menuConfigLoader = new ConfigLoader(rootDataFolder, menuFile);

			try {
				Config menuConfig = menuConfigLoader.load();
				loadedMenus.add(MenuParser.loadMenu(menuConfig, errorCollector));
			} catch (ConfigException e) {
				logConfigInitException(errorCollector, menuConfigLoader.getFile(), e);
			}
		}

		return loadedMenus;
	}

	private boolean isYamlFile(Path path) {
		return Files.isRegularFile(path) && path.getFileName().toString().toLowerCase().endsWith(".yml");
	}

}
