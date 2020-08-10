/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.commons.config;

import me.filoghost.commons.Preconditions;
import me.filoghost.commons.config.exception.ConfigLoadException;
import me.filoghost.commons.config.exception.ConfigSaveException;
import me.filoghost.commons.config.exception.ConfigSyntaxException;
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
	private final Path file;

	public ConfigLoader(Path rootDataFolder, Path file) {
		Preconditions.checkArgument(file.startsWith(rootDataFolder), "file \"" + file + "\" must be inside \"" + rootDataFolder + "\"");

		this.rootDataFolder = rootDataFolder;
		this.file = file;
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

		Path relativeConfigPath = rootDataFolder.relativize(file);
		String internalJarPath = toInternalJarPath(relativeConfigPath);

		try (InputStream defaultFile = getInternalResource(internalJarPath)) {
			if (defaultFile != null) {
				Files.copy(defaultFile, file);
			} else {
				Files.createFile(file);
			}
		} catch (IOException e) {
			throw new ConfigSaveException(ConfigErrors.createDefaultIOException, e);
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
		return Files.isRegularFile(file);
	}

	public Config load() throws ConfigLoadException {
		Preconditions.checkState(fileExists(), "\"" + file + "\" doesn't exist or is not a regular file");

		YamlConfiguration yaml = new YamlConfiguration();

		try (BufferedReader reader = Files.newBufferedReader(file)) {
			yaml.load(reader);
		} catch (IOException e) {
			throw new ConfigLoadException(ConfigErrors.readIOException, e);
		} catch (InvalidConfigurationException e) {
			throw new ConfigSyntaxException(ConfigErrors.invalidYamlSyntax, e);
		}

		return new Config(yaml, file);
	}

	public void save(Config config) throws ConfigSaveException {
		createParentDirectory();

		String data = config.saveToString();

		try (BufferedWriter writer = Files.newBufferedWriter(file)) {
			writer.write(data);
		} catch (IOException e) {
			throw new ConfigSaveException(ConfigErrors.writeDataIOException, e);
		}
	}

	private void createParentDirectory() throws ConfigSaveException {
		if (file.getParent() != null) {
			try {
				Files.createDirectories(file.getParent());
			} catch (IOException e) {
				throw new ConfigSaveException(ConfigErrors.createParentFolderIOException(rootDataFolder, file.getParent()), e);
			}
		}
	}

	public Path getFile() {
		return file;
	}

}
