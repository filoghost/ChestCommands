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
package me.filoghost.chestcommands.config.framework;

import me.filoghost.chestcommands.util.Preconditions;
import org.bukkit.configuration.file.YamlConfiguration;

import java.nio.file.Path;

public class Config extends ConfigSection {

	private final YamlConfiguration yaml;
	private final Path sourceFile;

	public Config(Path sourceFile) {
		this(new YamlConfiguration(), sourceFile);
	}

	public Config(YamlConfiguration yaml, Path sourceFile) {
		super(yaml);
		Preconditions.notNull(sourceFile, "sourceFile");
		this.yaml = yaml;
		this.sourceFile = sourceFile;
	}

	public Path getSourceFile() {
		return sourceFile;
	}

	public String saveToString() {
		return yaml.saveToString();
	}

	public void setHeader(String value) {
		yaml.options().header(value);
	}

}
