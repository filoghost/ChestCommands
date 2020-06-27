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
package me.filoghost.chestcommands;

import me.filoghost.chestcommands.command.CommandHandler;
import me.filoghost.chestcommands.command.framework.CommandFramework;
import me.filoghost.chestcommands.config.CustomPlaceholders;
import me.filoghost.chestcommands.config.Lang;
import me.filoghost.chestcommands.config.Settings;
import me.filoghost.chestcommands.config.yaml.Config;
import me.filoghost.chestcommands.config.yaml.ConfigLoader;
import me.filoghost.chestcommands.hook.BarAPIHook;
import me.filoghost.chestcommands.hook.BungeeCordHook;
import me.filoghost.chestcommands.hook.PlaceholderAPIHook;
import me.filoghost.chestcommands.hook.VaultEconomyHook;
import me.filoghost.chestcommands.legacy.UpgradeExecutorException;
import me.filoghost.chestcommands.legacy.UpgradesExecutor;
import me.filoghost.chestcommands.listener.CommandListener;
import me.filoghost.chestcommands.listener.InventoryListener;
import me.filoghost.chestcommands.listener.JoinListener;
import me.filoghost.chestcommands.listener.SignListener;
import me.filoghost.chestcommands.menu.AdvancedIconMenu;
import me.filoghost.chestcommands.menu.MenuManager;
import me.filoghost.chestcommands.menu.settings.MenuSettings;
import me.filoghost.chestcommands.parser.MenuParser;
import me.filoghost.chestcommands.task.RefreshMenusTask;
import me.filoghost.chestcommands.util.ErrorCollector;
import me.filoghost.chestcommands.util.Log;
import me.filoghost.chestcommands.util.Utils;
import me.filoghost.updatechecker.UpdateChecker;
import org.bstats.bukkit.MetricsLite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChestCommands extends JavaPlugin {


	public static final String CHAT_PREFIX = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "ChestCommands" + ChatColor.DARK_GREEN + "] " + ChatColor.GREEN;

	
	private static ChestCommands instance;

	private ConfigLoader settingsConfigLoader;
	private ConfigLoader placeholdersConfigLoader;
	private ConfigLoader langConfigLoader;


	private MenuManager menuManager;
	private static Settings settings;
	private static Lang lang;
	private static CustomPlaceholders placeholders;

	private static ErrorCollector lastLoadErrors;
	private static String newVersion;

	@Override
	public void onEnable() {
		if (instance != null || System.getProperty("ChestCommandsLoaded") != null) {
			Log.warning("Please do not use /reload or plugin reloaders. Use the command \"/cc reload\" instead.");
			return;
		}

		System.setProperty("ChestCommandsLoaded", "true");

		instance = this;
		Log.setLogger(getLogger());

		settingsConfigLoader = new ConfigLoader(getDataPath("config.yml"));
		placeholdersConfigLoader = new ConfigLoader(getDataPath("custom-placeholders.yml"));
		langConfigLoader = new ConfigLoader(getDataPath("lang.yml"));

		menuManager = new MenuManager();
		settings = new Settings();
		lang = new Lang();
		placeholders = new CustomPlaceholders();
		
		if (!Utils.isClassLoaded("org.bukkit.inventory.ItemFlag")) { // ItemFlag was added in 1.8
			if (Bukkit.getVersion().contains("(MC: 1.8)")) {
				criticalShutdown("ChestCommands requires a more recent version of Bukkit 1.8 to run.");
			} else {
				criticalShutdown("ChestCommands requires at least Bukkit 1.8 to run.");
			}
			return;
		}
		
		VaultEconomyHook.INSTANCE.setup();
		BarAPIHook.INSTANCE.setup();
		PlaceholderAPIHook.INSTANCE.setup();
		BungeeCordHook.INSTANCE.setup();
		
		if (!VaultEconomyHook.INSTANCE.isEnabled()) {
			Log.warning("Couldn't find Vault and a compatible economy plugin! Money-related features will not work.");
		}

		if (BarAPIHook.INSTANCE.isEnabled()) {
			Log.info("Hooked BarAPI");
		}

		if (PlaceholderAPIHook.INSTANCE.isEnabled()) {
			Log.info("Hooked PlaceholderAPI");
		}

		if (settings.update_notifications) {
			UpdateChecker.run(this, 56919, (String newVersion) -> {
				ChestCommands.newVersion = newVersion;

				Log.info("Found a new version: " + newVersion + " (yours: v" + getDescription().getVersion() + ")");
				Log.info("Download the update on Bukkit Dev:");
				Log.info("https://dev.bukkit.org/projects/chest-commands");
			});
		}

		// Start bStats metrics
		int pluginID = 3658;
		new MetricsLite(this, pluginID);

		Bukkit.getPluginManager().registerEvents(new CommandListener(menuManager), this);
		Bukkit.getPluginManager().registerEvents(new InventoryListener(menuManager), this);
		Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
		Bukkit.getPluginManager().registerEvents(new SignListener(menuManager), this);

		CommandFramework.register(this, new CommandHandler(menuManager, "chestcommands"));

		ErrorCollector errorCollector = load();
		
		if (errorCollector.hasWarningsOrErrors()) {
			Bukkit.getScheduler().runTaskLater(this, errorCollector::logToConsole, 10L);
		}

		Bukkit.getScheduler().runTaskTimer(this, new RefreshMenusTask(), 2L, 2L);
	}


	@Override
	public void onDisable() {
		closeAllMenus();
	}


	public ErrorCollector load() {
		ErrorCollector errors = new ErrorCollector();
		menuManager.clear();
		boolean isFreshInstall = !Files.isDirectory(getDataPath());
		try {
			Files.createDirectories(getDataPath());
		} catch (IOException e) {
			errors.addError("Plugin failed to load, couldn't create data folder.");
			return errors;
		}

		try {
			new UpgradesExecutor(this).run(isFreshInstall);
		} catch (UpgradeExecutorException e) {
			Log.severe("Encountered errors while running run automatic configuration upgrades. Some configuration files or menus may require manual updates.", e);
		}

		try {
			settingsConfigLoader.createDefault(this);
			settings.load(settingsConfigLoader);
		} catch (Throwable t) {
			logConfigLoadException(settingsConfigLoader, t);
		}

		try {
			langConfigLoader.createDefault(this);
			lang.load(langConfigLoader);
		} catch (Throwable t) {
			logConfigLoadException(langConfigLoader, t);
		}

		try {
			placeholdersConfigLoader.createDefault(this);
			Config placeholdersConfig = placeholdersConfigLoader.load();
			placeholders.load(placeholdersConfig, errors);
		} catch (Throwable t) {
			logConfigLoadException(placeholdersConfigLoader, t);
		}

		// Load the menus
		Path menusPath = getMenusPath();

		if (!Files.isDirectory(menusPath)) {
			// Create the menu folder with the example menu
			try {
				Files.createDirectories(menusPath);
			} catch (IOException e) {
				Log.severe("Couldn't create \"" + menusPath.getFileName() + "\" folder");
			}

			ConfigLoader exampleMenuLoader = new ConfigLoader(getDataPath(Paths.get("menu", "example.yml")));
			try {
				exampleMenuLoader.createDefault(this);
			} catch (Throwable t) {
				logConfigLoadException(exampleMenuLoader, t);
			}
		}

		List<Path> menuPaths;

		try {
			menuPaths = getMenusPathList();
		} catch (IOException e) {
			Log.severe("Couldn't fetch files inside the folder \"" + getMenusPath().getFileName() + "\"", e);
			menuPaths = Collections.emptyList();
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

			MenuSettings menuSettings = MenuParser.loadMenuSettings(menuConfig, errors);
			AdvancedIconMenu iconMenu = MenuParser.loadMenu(menuConfig, menuSettings.getTitle(), menuSettings.getRows(), errors);

			menuManager.registerMenu(menuConfig.getFileName(), menuSettings.getCommands(), iconMenu, errors);

			iconMenu.setRefreshTicks(menuSettings.getRefreshTenths());

			if (menuSettings.getOpenActions() != null) {
				iconMenu.setOpenActions(menuSettings.getOpenActions());
			}

			if (menuSettings.getOpenTrigger() != null) {
				menuManager.registerTriggerItem(menuSettings.getOpenTrigger(), iconMenu);
			}
		}
		
		ChestCommands.lastLoadErrors = errors;
		return errors;
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

	public ConfigLoader getLangConfigLoader() {
		return langConfigLoader;
	}

	public ConfigLoader getSettingsConfigLoader() {
		return settingsConfigLoader;
	}

	public ConfigLoader getPlaceholdersConfigLoader() {
		return placeholdersConfigLoader;
	}

	public Path getMenusPath() {
		return getDataPath("menu");
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


	public static void closeAllMenus() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (MenuManager.getOpenMenu(player) != null) {
				player.closeInventory();
			}
		}
	}


	public static ChestCommands getInstance() {
		return instance;
	}

	public Path getDataPath() {
		return getDataFolder().toPath();
	}

	public Path getDataPath(Path path) {
		return getDataPath().resolve(path);
	}

	public Path getDataPath(String path) {
		return getDataPath().resolve(path);
	}

	public MenuManager getMenuManager() {
		return menuManager;
	}

	public static Settings getSettings() {
		return settings;
	}

	public static Lang getLang() {
		return lang;
	}

	public static CustomPlaceholders getCustomPlaceholders() {
		return placeholders;
	}

	public static boolean hasNewVersion() {
		return newVersion != null;
	}

	public static String getNewVersion() {
		return newVersion;
	}
	
	public static ErrorCollector getLastLoadErrors() {
		return lastLoadErrors;
	}

	private static void criticalShutdown(String... errorMessage) {
		String separator = "****************************************************************************";

		List<String> output = new ArrayList<>();

		output.add(" ");
		output.add(separator);
		for (String line : errorMessage) {
			output.add("    " + line);
		}
		output.add(" ");
		output.add("    This plugin has been disabled.");
		output.add(separator);
		output.add(" ");
		
		System.out.println("\n" + output);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException ignored) {}
		instance.setEnabled(false);
	}

}
