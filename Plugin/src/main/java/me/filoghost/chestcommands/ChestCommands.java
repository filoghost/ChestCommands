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
import me.filoghost.chestcommands.config.AsciiPlaceholders;
import me.filoghost.chestcommands.config.Lang;
import me.filoghost.chestcommands.config.Settings;
import me.filoghost.chestcommands.config.yaml.PluginConfig;
import me.filoghost.chestcommands.hook.BarAPIHook;
import me.filoghost.chestcommands.hook.BungeeCordHook;
import me.filoghost.chestcommands.hook.PlaceholderAPIHook;
import me.filoghost.chestcommands.hook.VaultEconomyHook;
import me.filoghost.chestcommands.legacy.ConversionException;
import me.filoghost.chestcommands.legacy.LegacyConverter;
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
import me.filoghost.chestcommands.util.FileUtils;
import me.filoghost.chestcommands.util.Utils;
import me.filoghost.updatechecker.UpdateChecker;
import org.bstats.bukkit.MetricsLite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ChestCommands extends JavaPlugin {


	public static final String CHAT_PREFIX = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "ChestCommands" + ChatColor.DARK_GREEN + "] " + ChatColor.GREEN;

	
	private static ChestCommands instance;
	private MenuManager menuManager;
	private static Settings settings;
	private static Lang lang;

	private static ErrorCollector lastLoadErrors;
	private static String newVersion;

	@Override
	public void onEnable() {
		if (instance != null) {
			getLogger().warning("Please do not use /reload or plugin reloaders. Do \"/cc reload\" instead.");
			return;
		}

		instance = this;
		menuManager = new MenuManager();
		settings = new Settings();
		lang = new Lang();
		
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
			getLogger().warning("Couldn't find Vault and a compatible economy plugin! Money-related features will not work.");
		}

		if (BarAPIHook.INSTANCE.isEnabled()) {
			getLogger().info("Hooked BarAPI");
		}

		if (PlaceholderAPIHook.INSTANCE.isEnabled()) {
			getLogger().info("Hooked PlaceholderAPI");
		}

		if (settings.update_notifications) {
			UpdateChecker.run(this, 56919, (String newVersion) -> {
				ChestCommands.newVersion = newVersion;
				
				getLogger().info("Found a new version: " + newVersion + " (yours: v" + getDescription().getVersion() + ")");
				getLogger().info("Download the update on Bukkit Dev:");
				getLogger().info("https://dev.bukkit.org/projects/chest-commands");
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
		boolean isFreshInstall = !getDataFolder().isDirectory();
		getDataFolder().mkdirs();

		try {
			new LegacyConverter(this).run(isFreshInstall);
		} catch (ConversionException e) {
			getLogger().log(Level.SEVERE, "Couldn't run automatic configuration upgrades. The plugin may not work correctly.", e);
		}

		try {
			PluginConfig settingsYaml = getSettingsConfig();
			settingsYaml.load();
			settings.load(settingsYaml);
		} catch (IOException e) {
			e.printStackTrace();
			getLogger().warning("I/O error while using the configuration. Default values will be used.");
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			getLogger().warning("The config.yml was not a valid YAML, please look at the error above. Default values will be used.");
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().warning("Unhandled error while reading the values for the configuration! Please inform the developer.");
		}

		try {
			PluginConfig langYaml = getLangConfig();
			langYaml.load();
			lang.load(langYaml);
		} catch (IOException e) {
			e.printStackTrace();
			getLogger().warning("I/O error while using the language file. Default values will be used.");
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			getLogger().warning("The lang.yml was not a valid YAML, please look at the error above. Default values will be used.");
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().warning("Unhandled error while reading the values for the configuration! Please inform the developer.");
		}

		try {
			AsciiPlaceholders.load(errors);
		} catch (IOException e) {
			e.printStackTrace();
			getLogger().warning("I/O error while reading the placeholders. They will not work.");
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().warning("Unhandled error while reading the placeholders! Please inform the developer.");
		}


		// Load the menus
		File menusFolder = getMenusFolder();

		if (!menusFolder.isDirectory()) {
			// Create the directory with the default menu
			menusFolder.mkdirs();
			FileUtils.saveResourceSafe(this, "menu" + File.separator + "example.yml");
		}

		List<PluginConfig> menusList = loadMenus(menusFolder);
		for (PluginConfig menuConfig : menusList) {
			try {
				menuConfig.load();
			} catch (IOException e) {
				e.printStackTrace();
				errors.addError("I/O error while loading the menu \"" + menuConfig.getFileName() + "\". Is the file in use?");
				continue;
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
				errors.addError("Invalid YAML configuration for the menu \"" + menuConfig.getFileName() + "\". Please look at the error above, or use an online YAML parser (google is your friend).");
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

	public PluginConfig getLangConfig() {
		return new PluginConfig(this, "lang.yml");
	}

	public PluginConfig getSettingsConfig() {
		return new PluginConfig(this, "config.yml");
	}

	public File getMenusFolder() {
		return new File(getDataFolder(), "menu");
	}

	/**
	 * Loads all the configuration files recursively into a list.
	 */
	public List<PluginConfig> loadMenus(File file) {
		List<PluginConfig> list = new ArrayList<>();
		if (file.isDirectory()) {
			for (File subFile : file.listFiles()) {
				list.addAll(loadMenus(subFile));
			}
		} else if (file.isFile()) {
			if (file.getName().endsWith(".yml")) {
				list.add(new PluginConfig(this, file));
			}
		}
		return list;
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

	public MenuManager getMenuManager() {
		return menuManager;
	}

	public static Settings getSettings() {
		return settings;
	}

	public static Lang getLang() {
		return lang;
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
