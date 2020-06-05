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

import org.bstats.bukkit.MetricsLite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.filoghost.chestcommands.bridge.BarAPIBridge;
import me.filoghost.chestcommands.bridge.EconomyBridge;
import me.filoghost.chestcommands.bridge.PlaceholderAPIBridge;
import me.filoghost.chestcommands.command.CommandHandler;
import me.filoghost.chestcommands.command.framework.CommandFramework;
import me.filoghost.chestcommands.config.AsciiPlaceholders;
import me.filoghost.chestcommands.config.Lang;
import me.filoghost.chestcommands.config.Settings;
import me.filoghost.chestcommands.config.yaml.PluginConfig;
import me.filoghost.chestcommands.internal.BoundItem;
import me.filoghost.chestcommands.internal.ExtendedIconMenu;
import me.filoghost.chestcommands.internal.MenuData;
import me.filoghost.chestcommands.internal.MenuInventoryHolder;
import me.filoghost.chestcommands.listener.CommandListener;
import me.filoghost.chestcommands.listener.InventoryListener;
import me.filoghost.chestcommands.listener.JoinListener;
import me.filoghost.chestcommands.listener.SignListener;
import me.filoghost.chestcommands.serializer.CommandSerializer;
import me.filoghost.chestcommands.serializer.MenuSerializer;
import me.filoghost.chestcommands.task.ErrorLoggerTask;
import me.filoghost.chestcommands.task.RefreshMenusTask;
import me.filoghost.chestcommands.util.BukkitUtils;
import me.filoghost.chestcommands.util.CaseInsensitiveMap;
import me.filoghost.chestcommands.util.ErrorLogger;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChestCommands extends JavaPlugin {

	public static final String CHAT_PREFIX = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "ChestCommands" + ChatColor.DARK_GREEN + "] " + ChatColor.GREEN;

	private static ChestCommands instance;
	private static Settings settings;
	private static Lang lang;

	private static Map<String, ExtendedIconMenu> fileNameToMenuMap;
	private static Map<String, ExtendedIconMenu> commandsToMenuMap;

	private static Set<BoundItem> boundItems;

	private static int lastReloadErrors;
	private static String newVersion;

	@Override
	public void onEnable() {
		if (instance != null) {
			getLogger().warning("Please do not use /reload or plugin reloaders. Do \"/cc reload\" instead.");
			return;
		}

		instance = this;
		fileNameToMenuMap = CaseInsensitiveMap.create();
		commandsToMenuMap = CaseInsensitiveMap.create();
		boundItems = new HashSet<>();

		settings = new Settings(new PluginConfig(this, "config.yml"));
		lang = new Lang(new PluginConfig(this, "lang.yml"));

		if (!EconomyBridge.setupEconomy()) {
			getLogger().warning("Vault with a compatible economy plugin was not found! Icons with a PRICE or commands that give money will not work.");
		}

		if (BarAPIBridge.setupPlugin()) {
			getLogger().info("Hooked BarAPI");
		}

		if (PlaceholderAPIBridge.setupPlugin()) {
			getLogger().info("Hooked PlaceholderAPI");
		}

		if (settings.update_notifications) {
			new SimpleUpdater(this, 56919).checkForUpdates((String newVersion) -> {
				ChestCommands.newVersion = newVersion;

				if (settings.use_console_colors) {
					Bukkit.getConsoleSender().sendMessage(CHAT_PREFIX + "Found a new version: " + newVersion + ChatColor.WHITE + " (yours: v" + getDescription().getVersion() + ")");
					Bukkit.getConsoleSender().sendMessage(CHAT_PREFIX + ChatColor.WHITE + "Download it on Bukkit Dev:");
					Bukkit.getConsoleSender().sendMessage(CHAT_PREFIX + ChatColor.WHITE + "dev.bukkit.org/bukkit-plugins/chest-commands");
				} else {
					getLogger().info("Found a new version available: " + newVersion);
					getLogger().info("Download it on Bukkit Dev:");
					getLogger().info("dev.bukkit.org/bukkit-plugins/chest-commands");
				}
			});
		}

		// Start bStats metrics
		new MetricsLite(this);

		Bukkit.getPluginManager().registerEvents(new CommandListener(), this);
		Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
		Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
		Bukkit.getPluginManager().registerEvents(new SignListener(), this);

		CommandFramework.register(this, new CommandHandler("chestcommands"));

		ErrorLogger errorLogger = new ErrorLogger();
		load(errorLogger);

		lastReloadErrors = errorLogger.getSize();
		if (errorLogger.hasErrors()) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new ErrorLoggerTask(errorLogger), 10L);
		}

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new RefreshMenusTask(), 2L, 2L);
	}


	@Override
	public void onDisable() {
		closeAllMenus();
	}


	public void load(ErrorLogger errorLogger) {
		fileNameToMenuMap.clear();
		commandsToMenuMap.clear();
		boundItems.clear();

		CommandSerializer.checkClassConstructors(errorLogger);

		try {
			settings.load();
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
			lang.load();
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
			AsciiPlaceholders.load(errorLogger);
		} catch (IOException e) {
			e.printStackTrace();
			getLogger().warning("I/O error while reading the placeholders. They will not work.");
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().warning("Unhandled error while reading the placeholders! Please inform the developer.");
		}

		// Load the menus
		File menusFolder = new File(getDataFolder(), "menu");

		if (!menusFolder.isDirectory()) {
			// Create the directory with the default menu
			menusFolder.mkdirs();
			BukkitUtils.saveResourceSafe(this, "menu" + File.separator + "example.yml");
		}

		List<PluginConfig> menusList = loadMenus(menusFolder);
		for (PluginConfig menuConfig : menusList) {
			try {
				menuConfig.load();
			} catch (IOException e) {
				e.printStackTrace();
				errorLogger.addError("I/O error while loading the menu \"" + menuConfig.getFileName() + "\". Is the file in use?");
				continue;
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
				errorLogger.addError("Invalid YAML configuration for the menu \"" + menuConfig.getFileName() + "\". Please look at the error above, or use an online YAML parser (google is your friend).");
				continue;
			}

			MenuData data = MenuSerializer.loadMenuData(menuConfig, errorLogger);
			ExtendedIconMenu iconMenu = MenuSerializer.loadMenu(menuConfig, data.getTitle(), data.getRows(), errorLogger);

			if (fileNameToMenuMap.containsKey(menuConfig.getFileName())) {
				errorLogger.addError("Two menus have the same file name \"" + menuConfig.getFileName() + "\" with different cases. There will be problems opening one of these two menus.");
			}
			fileNameToMenuMap.put(menuConfig.getFileName(), iconMenu);

			if (data.hasCommands()) {
				for (String command : data.getCommands()) {
					if (!command.isEmpty()) {
						if (commandsToMenuMap.containsKey(command)) {
							errorLogger.addError("The menus \"" + commandsToMenuMap.get(command).getFileName() + "\" and \"" + menuConfig.getFileName() + "\" have the same command \"" + command + "\". Only one will be opened.");
						}
						commandsToMenuMap.put(command, iconMenu);
					}
				}
			}

			iconMenu.setRefreshTicks(data.getRefreshTenths());

			if (data.getOpenActions() != null) {
				iconMenu.setOpenActions(data.getOpenActions());
			}

			if (data.hasBoundMaterial() && data.getClickType() != null) {
				BoundItem boundItem = new BoundItem(iconMenu, data.getBoundMaterial(), data.getClickType());
				if (data.hasBoundDataValue()) {
					boundItem.setRestrictiveData(data.getBoundDataValue());
				}
				boundItems.add(boundItem);
			}
		}

		// Register the BungeeCord plugin channel
		if (!Bukkit.getMessenger().isOutgoingChannelRegistered(this, "BungeeCord")) {
			Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		}
	}


	/**
	 * Loads all the configuration files recursively into a list.
	 */
	private List<PluginConfig> loadMenus(File file) {
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
		for (Player player : BukkitUtils.getOnlinePlayers()) {
			if (player.getOpenInventory() != null) {
				if (player.getOpenInventory().getTopInventory().getHolder() instanceof MenuInventoryHolder || player.getOpenInventory().getBottomInventory().getHolder() instanceof MenuInventoryHolder) {
					player.closeInventory();
				}
			}
		}
	}


	public static ChestCommands getInstance() {
		return instance;
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

	public static Map<String, ExtendedIconMenu> getFileNameToMenuMap() {
		return fileNameToMenuMap;
	}

	public static Map<String, ExtendedIconMenu> getCommandToMenuMap() {
		return commandsToMenuMap;
	}

	public static Set<BoundItem> getBoundItems() {
		return boundItems;
	}

	public static int getLastReloadErrors() {
		return lastReloadErrors;
	}

	public static void setLastReloadErrors(int lastReloadErrors) {
		ChestCommands.lastReloadErrors = lastReloadErrors;
	}

}
