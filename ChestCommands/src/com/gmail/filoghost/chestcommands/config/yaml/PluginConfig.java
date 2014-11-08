package com.gmail.filoghost.chestcommands.config.yaml;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * A simple utility class to manage configurations with a file associated to them.
 */
public class PluginConfig extends YamlConfiguration {

	private File file;
	private Plugin plugin;
	
	public PluginConfig(Plugin plugin, File file) {
		super();
		this.file = file;
		this.plugin = plugin;
	}
	
	public PluginConfig(Plugin plugin, String name) {
		this(plugin, new File(plugin.getDataFolder(), name));
	}

	public void load() throws IOException, InvalidConfigurationException {
		
		if (!file.isFile()) {
			if (plugin.getResource(file.getName()) != null) {
				plugin.saveResource(file.getName(), false);
			} else {
				if (file.getParentFile() != null) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
		}
		
		// To reset all the values when loading.
		for (String section : this.getKeys(false)) {
			set(section, null);
		}
	    load(file);
	}
	
	public void save() throws IOException {
		this.save(file);
	}
	
	public Plugin getPlugin() {
		return plugin;
	}
	
	public String getFileName() {
		return file.getName();
	}
}
