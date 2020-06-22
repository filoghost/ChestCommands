package me.filoghost.chestcommands.legacy;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.config.yaml.PluginConfig;
import me.filoghost.chestcommands.legacy.UpgradesDoneRegistry.Upgrade;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;

public class LegacyConverter {

	private final ChestCommands plugin;

	public LegacyConverter(ChestCommands plugin) {
		this.plugin = plugin;
	}

	public void run(boolean isFreshInstall) throws ConversionException {
		Path upgradesDoneFile = plugin.getDataFolder().toPath().resolve(".upgrades-done");
		UpgradesDoneRegistry upgradesDone;

		try {
			upgradesDone = new UpgradesDoneRegistry(upgradesDoneFile);
		} catch (IOException e) {
			throw new ConversionException("Couldn't read upgrades metadata file \"" + upgradesDoneFile.getFileName() + "\"", e);
		}

		if (isFreshInstall) {
			// Mark all upgrades as already done, assuming default configuration files are up to date
			upgradesDone.setAllDone();

		} else {
			PluginConfig settingsConfig = plugin.getSettingsConfig();
			loadConfig(settingsConfig);
			LegacySettingsConverter legacySettingsConverter = new LegacySettingsConverter(settingsConfig);

			String legacyCommandSeparator = legacySettingsConverter.getLegacyCommandSeparator().orElse(";");

			upgradesDone.runIfNecessary(
					Upgrade.V4_MENUS,
					() -> convertMenus(legacyCommandSeparator));

			upgradesDone.runIfNecessary(
					Upgrade.V4_CONFIG,
					() -> convertIfLegacy(settingsConfig, legacySettingsConverter));
		}

		try {
			upgradesDone.save();
		} catch (IOException e) {
			throw new ConversionException("Couldn't save upgrades metadata file \"" + upgradesDoneFile.getFileName() + "\"", e);
		}
	}

	private void loadConfig(PluginConfig config) throws ConversionException {
		try {
			config.load();
		} catch (IOException e) {
			throw new ConversionException("Couldn't read configuration file \"" + config.getFileName() + "\"", e);
		} catch (InvalidConfigurationException e) {
			throw new ConversionException("Couldn't parse YAML syntax for file \"" + config.getFileName() + "\"", e);
		}
	}

	private void convertMenus(String legacyCommandSeparator) throws ConversionException {
		File menusFolder = plugin.getMenusFolder();

		List<PluginConfig> menuConfigs = plugin.loadMenus(menusFolder);
		for (PluginConfig menuConfig : menuConfigs) {
			loadConfig(menuConfig);
			convertMenu(menuConfig, legacyCommandSeparator);
		}
	}

	private void convertMenu(PluginConfig menuConfig, String legacyCommandSeparator) {
		LegacyMenuConverter legacyMenuConverter = new LegacyMenuConverter(menuConfig, legacyCommandSeparator);
		convertIfLegacy(menuConfig, legacyMenuConverter);
	}

	private void convertIfLegacy(PluginConfig config, ConfigConverter legacyConverter) {
		boolean modified = legacyConverter.convert();
		if (modified) {
			try {
				createBackupFile(config.getFile());
			} catch (IOException e) {
				plugin.getLogger().log(Level.SEVERE, "Couldn't create backup of " + config.getFileName(), e);
				return;
			}

			plugin.getLogger().info(
					"Automatically updated configuration file " + config.getFileName() + " with newer configuration nodes. " +
					"A backup of the old file has been saved.");

			try {
				config.save();
			} catch (IOException e) {
				plugin.getLogger().log(Level.SEVERE, "Couldn't save modified file: ", e);
			}
		}
	}

	private void createBackupFile(File file) throws IOException {
		String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd_HH.mm"));
		String backupName = file.getName() + "-" + date + ".backup";
		Files.copy(file.toPath(), file.toPath().resolveSibling(backupName), StandardCopyOption.REPLACE_EXISTING);
	}



}
