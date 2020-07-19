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

import me.filoghost.chestcommands.api.internal.BackendAPI;
import me.filoghost.chestcommands.command.CommandHandler;
import me.filoghost.chestcommands.command.framework.CommandFramework;
import me.filoghost.chestcommands.config.ConfigManager;
import me.filoghost.chestcommands.config.CustomPlaceholders;
import me.filoghost.chestcommands.config.Lang;
import me.filoghost.chestcommands.config.Settings;
import me.filoghost.chestcommands.config.framework.ConfigLoader;
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
import me.filoghost.chestcommands.logging.ErrorMessages;
import me.filoghost.chestcommands.logging.PrintableErrorCollector;
import me.filoghost.chestcommands.menu.MenuManager;
import me.filoghost.chestcommands.parsing.menu.LoadedMenu;
import me.filoghost.chestcommands.task.TickingTask;
import me.filoghost.chestcommands.util.Utils;
import me.filoghost.chestcommands.util.logging.ErrorCollector;
import me.filoghost.chestcommands.util.logging.Log;
import me.filoghost.updatechecker.UpdateChecker;
import org.bstats.bukkit.MetricsLite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ChestCommands extends JavaPlugin {


	public static final String CHAT_PREFIX = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "ChestCommands" + ChatColor.DARK_GREEN + "] " + ChatColor.GREEN;

	
	private static ChestCommands instance;

	private ConfigManager configManager;
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
		BackendAPI.setImplementation(new DefaultBackendAPI());

		configManager = new ConfigManager(getDataFolder().toPath());
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
		
		if (errorCollector.hasErrors()) {
			errorCollector.logToConsole();
			Bukkit.getScheduler().runTaskLater(this, () -> {
				Bukkit.getConsoleSender().sendMessage(
						ChatColor.RED + "[ChestCommands] Encountered " + errorCollector.getErrorsCount() + " on load. "
						+ "Check previous console logs or run \"/chestcommands errors\" to see them again.");
			}, 10L);
		}

		Bukkit.getScheduler().runTaskTimer(this, new TickingTask(), 1L, 1L);
	}


	@Override
	public void onDisable() {
		closeAllMenus();
	}


	public ErrorCollector load() {
		ErrorCollector errorCollector = new PrintableErrorCollector();
		menuManager.clear();
		boolean isFreshInstall = !Files.isDirectory(configManager.getRootDataFolder());
		try {
			Files.createDirectories(configManager.getRootDataFolder());
		} catch (IOException e) {
			errorCollector.add(ErrorMessages.Config.createDataFolderIOException, e);
			return errorCollector;
		}

		try {
			new UpgradesExecutor(configManager).run(isFreshInstall, errorCollector);
		} catch (UpgradeExecutorException e) {
			errorCollector.add(ErrorMessages.Upgrade.genericExecutorError, e);
		}

		settings = configManager.tryLoadSettings(errorCollector);
		lang = configManager.tryLoadLang(errorCollector);
		placeholders = configManager.tryLoadCustomPlaceholders(errorCollector);

		// Create the menu folder with the example menu
		if (!Files.isDirectory(configManager.getMenusFolder())) {
			ConfigLoader exampleMenuLoader = configManager.getConfigLoader(configManager.getMenusFolder().resolve("example.yml"));
			configManager.tryCreateDefault(errorCollector, exampleMenuLoader);
		}

		List<LoadedMenu> loadedMenus = configManager.tryLoadMenus(errorCollector);
		for (LoadedMenu loadedMenu : loadedMenus) {
			menuManager.registerMenu(loadedMenu, errorCollector);
		}

		ChestCommands.lastLoadErrors = errorCollector;
		return errorCollector;
	}

	public static void closeAllMenus() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (MenuManager.getOpenItemInventory(player) != null) {
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
