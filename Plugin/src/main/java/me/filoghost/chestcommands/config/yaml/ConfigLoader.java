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

import me.filoghost.chestcommands.ChestCommands;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
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

	public void createDefault(ChestCommands plugin) throws IOException {
		if (!path.startsWith(plugin.getDataPath())) {
			throw new IOException("Config file " + path + " must be inside " + plugin.getDataPath());
		}

		if (Files.exists(path)) {
			return;
		}

		if (path.getParent() != null) {
			Files.createDirectories(path.getParent());
		}

		Path absoluteDataPath = plugin.getDataPath().toAbsolutePath();
		Path absoluteConfigPath = path.toAbsolutePath();

		if (absoluteConfigPath.startsWith(absoluteDataPath)) {
			Path relativeConfigPath = absoluteDataPath.relativize(absoluteConfigPath);
			String defaultConfigURL = StreamSupport.stream(relativeConfigPath.spliterator(), false)
					.map(Path::toString)
					.collect(Collectors.joining("/"));

			try (InputStream defaultFile = plugin.getResource(defaultConfigURL)) {
				if (defaultFile != null) {
					Files.copy(defaultFile, path);
					return;
				}
			}
		}

		Files.createFile(path);
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
