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

import org.bukkit.configuration.file.YamlConfiguration;

import java.nio.file.Path;

public class Config extends ConfigSection {

	private final YamlConfiguration yaml;
	private final Path sourceFilePath;

	public Config(Path sourceFilePath) {
		this(new YamlConfiguration(), sourceFilePath);
	}

	public Config(YamlConfiguration yaml, Path sourceFilePath) {
		super(yaml);
		this.yaml = yaml;
		this.sourceFilePath = sourceFilePath;
	}

	public Path getSourceFile() {
		return sourceFilePath;
	}

	public String saveToString() {
		return yaml.saveToString();
	}

	public void setHeader(String value) {
		yaml.options().header(value);
	}

}
