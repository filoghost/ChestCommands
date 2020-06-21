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
package me.filoghost.chestcommands.config.yaml;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

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

		// To reset all the values when loading
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

	public File getFile() {
		return file;
	}

	public String getFileName() {
		return file.getName();
	}

}
