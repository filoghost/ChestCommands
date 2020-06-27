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

import me.filoghost.chestcommands.config.files.CustomPlaceholders;
import me.filoghost.chestcommands.config.files.Lang;
import me.filoghost.chestcommands.config.files.LoadedMenu;
import me.filoghost.chestcommands.config.files.Settings;
import me.filoghost.chestcommands.menu.AdvancedIconMenu;
import me.filoghost.chestcommands.menu.settings.MenuSettings;
import me.filoghost.chestcommands.parser.MenuParser;
import me.filoghost.chestcommands.util.ErrorCollector;
import me.filoghost.chestcommands.util.Log;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigManager {

	private final Path baseDataPath;
	private final ConfigLoader settingsConfigLoader;
	private final ConfigLoader placeholdersConfigLoader;
	private final ConfigLoader langConfigLoader;

	public ConfigManager(Path baseDataPath) {
		this.baseDataPath = baseDataPath;
		settingsConfigLoader = new ConfigLoader(baseDataPath.resolve("config.yml"));
		placeholdersConfigLoader = new ConfigLoader(baseDataPath.resolve("custom-placeholders.yml"));
		langConfigLoader = new ConfigLoader(baseDataPath.resolve("lang.yml"));
	}

	public Settings tryLoadSettings() {
		Settings settings = new Settings();

		try {
			settingsConfigLoader.createDefault(baseDataPath);
			settings.load(settingsConfigLoader);
		} catch (Throwable t) {
			logConfigLoadException(settingsConfigLoader, t);
		}

		return settings;
	}

	public Lang tryLoadLang() {
		Lang lang = new Lang();

		try {
			langConfigLoader.createDefault(baseDataPath);
			lang.load(langConfigLoader);
		} catch (Throwable t) {
			logConfigLoadException(langConfigLoader, t);
		}

		return lang;
	}

	public CustomPlaceholders tryLoadCustomPlaceholders(ErrorCollector errorCollector) {
		CustomPlaceholders placeholders = new CustomPlaceholders();

		try {
			placeholdersConfigLoader.createDefault(baseDataPath);
			Config placeholdersConfig = placeholdersConfigLoader.load();
			placeholders.load(placeholdersConfig, errorCollector);
		} catch (Throwable t) {
			logConfigLoadException(placeholdersConfigLoader, t);
		}

		return placeholders;
	}

	public void tryCreateDefault(ConfigLoader configLoader) {
		try {
			configLoader.createDefault(baseDataPath);
		} catch (Throwable t) {
			logConfigLoadException(configLoader, t);
		}
	}

	public Path getMenusPath() {
		return baseDataPath.resolve("menu");
	}

	/**
	 * Returns a list of YML menu files.
	 */
	public List<Path> getMenusPathList() throws IOException {
		try (Stream<Path> paths = Files.walk(getMenusPath(), FileVisitOption.FOLLOW_LINKS)) {
			return paths.filter(Files::isRegularFile)
					.filter(this::isYmlPath)
					.collect(Collectors.toList());
		}
	}


	private boolean isYmlPath(Path path) {
		return path.getFileName().toString().toLowerCase().endsWith(".yml");
	}

	private void logConfigLoadException(ConfigLoader configLoader, Throwable t) {
		t.printStackTrace();

		if (t instanceof IOException) {
			Log.warning("Error while reading the file \"" + configLoader.getFileName() +  "\". Default values will be used.");
		} else if (t instanceof InvalidConfigurationException) {
			Log.warning("Invalid YAML syntax in the file \"" + configLoader.getFileName() + "\", please look at the error above. Default values will be used.");
		} else {
			Log.warning("Unhandled error while parsing the file \"" + configLoader.getFileName() + "\". Please inform the developer.");
		}
	}

	public Path getBaseDataPath() {
		return baseDataPath;
	}

	public ConfigLoader getSettingsConfigLoader() {
		return settingsConfigLoader;
	}

	public ConfigLoader getPlaceholdersConfigLoader() {
		return placeholdersConfigLoader;
	}

	public ConfigLoader getLangConfigLoader() {
		return langConfigLoader;
	}

	public List<LoadedMenu> tryLoadMenus(ErrorCollector errorCollector) {
		List<LoadedMenu> loadedMenus = new ArrayList<>();
		List<Path> menuPaths;

		try {
			menuPaths = getMenusPathList();
		} catch (IOException e) {
			Log.severe("Couldn't fetch files inside the folder \"" + getMenusPath().getFileName() + "\"", e);
			return Collections.emptyList();
		}

		for (Path menuFile : menuPaths) {
			ConfigLoader menuConfigLoader = new ConfigLoader(menuFile);
			Config menuConfig;

			try {
				menuConfig = menuConfigLoader.load();
			} catch (Throwable t) {
				logConfigLoadException(menuConfigLoader, t);
				continue;
			}

			MenuSettings menuSettings = MenuParser.loadMenuSettings(menuConfig, errorCollector);
			AdvancedIconMenu iconMenu = MenuParser.loadMenu(menuConfig, menuSettings.getTitle(), menuSettings.getRows(), errorCollector);

			iconMenu.setRefreshTicks(menuSettings.getRefreshTenths());

			if (menuSettings.getOpenActions() != null) {
				iconMenu.setOpenActions(menuSettings.getOpenActions());
			}

			loadedMenus.add(new LoadedMenu(menuConfig.getFileName(), menuSettings, iconMenu));
		}

		return loadedMenus;
	}
}
