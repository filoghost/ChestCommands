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
package me.filoghost.chestcommands.config;

import me.filoghost.chestcommands.config.framework.BaseConfigManager;
import me.filoghost.chestcommands.config.framework.Config;
import me.filoghost.chestcommands.config.framework.ConfigLoader;
import me.filoghost.chestcommands.config.framework.exception.ConfigException;
import me.filoghost.chestcommands.config.framework.mapped.MappedConfigLoader;
import me.filoghost.chestcommands.logging.ErrorMessages;
import me.filoghost.chestcommands.parsing.menu.LoadedMenu;
import me.filoghost.chestcommands.parsing.menu.MenuParser;
import me.filoghost.chestcommands.util.Preconditions;
import me.filoghost.chestcommands.util.logging.ErrorCollector;

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

	/**
	 * Returns a list of YML menu files.
	 */
	public List<Path> getMenuPaths() throws IOException {
		Preconditions.checkState(Files.isDirectory(getMenusFolder()), "menus folder doesn't exist");

		try (Stream<Path> paths = Files.walk(getMenusFolder(), FileVisitOption.FOLLOW_LINKS)) {
			return paths.filter(Files::isRegularFile)
					.filter(this::isYmlPath)
					.collect(Collectors.toList());
		}
	}

	private void logConfigInitException(ErrorCollector errorCollector, Path file, ConfigException e) {
		errorCollector.add(ErrorMessages.Config.initException(file), e);
	}

	public List<LoadedMenu> tryLoadMenus(ErrorCollector errorCollector) {
		List<LoadedMenu> loadedMenus = new ArrayList<>();
		List<Path> menuPaths;

		try {
			menuPaths = getMenuPaths();
		} catch (IOException e) {
			errorCollector.add(ErrorMessages.Config.menuListIOException(getMenusFolder()), e);
			return Collections.emptyList();
		}

		for (Path menuFile : menuPaths) {
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

	private boolean isYmlPath(Path path) {
		return path.getFileName().toString().toLowerCase().endsWith(".yml");
	}

}
