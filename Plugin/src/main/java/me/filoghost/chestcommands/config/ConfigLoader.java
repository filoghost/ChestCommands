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

import me.filoghost.chestcommands.util.Preconditions;
import org.bukkit.configuration.InvalidConfigurationException;

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

	private final Path path;

	public ConfigLoader(Path path) {
		this.path = path;
	}

	public Path getPath() {
		return path;
	}

	public void createDefault(Path baseDataPath) throws IOException {
		if (!path.startsWith(baseDataPath)) {
			throw new IOException("Config file " + path + " must be inside " + baseDataPath);
		}

		if (Files.exists(path)) {
			return;
		}

		if (path.getParent() != null) {
			Files.createDirectories(path.getParent());
		}

		Path absoluteDataPath = baseDataPath.toAbsolutePath();
		Path absoluteConfigPath = path.toAbsolutePath();

		if (absoluteConfigPath.startsWith(absoluteDataPath)) {
			Path relativeConfigPath = absoluteDataPath.relativize(absoluteConfigPath);
			String internalJarPath = toInternalJarPath(relativeConfigPath);

			try (InputStream defaultFile = getResource(internalJarPath)) {
				if (defaultFile != null) {
					Files.copy(defaultFile, path);
					return;
				}
			}
		}

		Files.createFile(path);
	}

	private String toInternalJarPath(Path path) {
		return StreamSupport.stream(path.spliterator(), false)
				.map(Path::toString)
				.collect(Collectors.joining("/", "/", ""));
	}


	private InputStream getResource(String internalJarPath) throws IOException {
		Preconditions.notNull(internalJarPath, "internalJarPath");

		URL resourceURL = getClass().getResource(internalJarPath);
		if (resourceURL == null) {
			throw new IOException("Couldn't find resource " + internalJarPath);
		}

		URLConnection connection = resourceURL.openConnection();
		connection.setUseCaches(false);
		return connection.getInputStream();
	}


	public Config load() throws IOException, InvalidConfigurationException {
		Config config = new Config(path);

		try (BufferedReader reader = Files.newBufferedReader(path)) {
			config.load(reader);
		}

		return config;
	}

	public Config loadEmpty() {
		return new Config(path);
	}

	public void save(Config config) throws IOException {
		if (path.getParent() != null) {
			Files.createDirectories(path.getParent());
		}

		String data = config.saveToString();

		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			writer.write(data);
		}
	}

	public String getFileName() {
		return path.getFileName().toString();
	}

}
