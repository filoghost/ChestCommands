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
package me.filoghost.chestcommands.config.framework;

import me.filoghost.chestcommands.config.framework.exception.ConfigLoadException;
import me.filoghost.chestcommands.config.framework.exception.ConfigSaveException;
import me.filoghost.chestcommands.config.framework.exception.ConfigSyntaxException;
import me.filoghost.chestcommands.util.Preconditions;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ConfigLoader {

	private final Path rootDataFolder;
	private final Path configPath;

	public ConfigLoader(Path rootDataFolder, Path configPath) {
		Preconditions.checkArgument(configPath.startsWith(rootDataFolder), "config file " + configPath + " cannot be outside " + rootDataFolder);

		this.rootDataFolder = rootDataFolder;
		this.configPath = configPath;
	}

	public Config init() throws ConfigSaveException, ConfigLoadException {
		createDefault();
		return load();
	}

	public void createDefault() throws ConfigSaveException {
		if (fileExists()) {
			return;
		}

		createParentDirectory();

		Path relativeConfigPath = rootDataFolder.relativize(configPath);
		String internalJarPath = toInternalJarPath(relativeConfigPath);

		try (InputStream defaultFile = getInternalResource(internalJarPath)) {
			if (defaultFile != null) {
				Files.copy(defaultFile, configPath);
			} else {
				Files.createFile(configPath);
			}
		} catch (IOException e) {
			throw new ConfigSaveException("couldn't create default config file " + configPath, e);
		}
	}

	private String toInternalJarPath(Path path) {
		return StreamSupport.stream(path.spliterator(), false)
				.map(Path::toString)
				.collect(Collectors.joining("/", "/", ""));
	}


	private InputStream getInternalResource(String internalJarPath) throws IOException {
		Preconditions.notNull(internalJarPath, "internalJarPath");

		URL resourceURL = getClass().getResource(internalJarPath);
		if (resourceURL == null) {
			return null;
		}

		URLConnection connection = resourceURL.openConnection();
		connection.setUseCaches(false);
		return connection.getInputStream();
	}

	public boolean fileExists() {
		return (Files.isRegularFile(configPath));
	}

	public Config load() throws ConfigLoadException {
		Preconditions.checkState(fileExists(), configPath.getFileName() + " doesn't exist or is not a regular file");

		YamlConfiguration yaml = new YamlConfiguration();

		try (BufferedReader reader = Files.newBufferedReader(configPath)) {
			yaml.load(reader);
		} catch (IOException e) {
			throw new ConfigLoadException("couldn't read config file " + configPath, e);
		} catch (InvalidConfigurationException e) {
			throw new ConfigSyntaxException(e.getMessage(), e);
		}

		return new Config(yaml, configPath);
	}

	public void save(Config config) throws ConfigSaveException {
		createParentDirectory();

		String data = config.saveToString();

		try (BufferedWriter writer = Files.newBufferedWriter(configPath)) {
			writer.write(data);
		} catch (IOException e) {
			throw new ConfigSaveException("couldn't write config data to " + configPath, e);
		}
	}

	private void createParentDirectory() throws ConfigSaveException {
		if (configPath.getParent() != null) {
			try {
				Files.createDirectories(configPath.getParent());
			} catch (IOException e) {
				throw new ConfigSaveException("couldn't create directory " + configPath.getParent(), e);
			}
		}
	}

	public Path getConfigPath() {
		return configPath;
	}

	public String getFileName() {
		return configPath.getFileName().toString();
	}

}
