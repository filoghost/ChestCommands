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
package me.filoghost.chestcommands.legacy.upgrades;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.config.yaml.Config;
import me.filoghost.chestcommands.config.yaml.ConfigLoader;
import me.filoghost.chestcommands.legacy.Upgrade;
import me.filoghost.chestcommands.legacy.UpgradeException;
import me.filoghost.chestcommands.util.Strings;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class PlaceholdersUpgrade extends Upgrade {

	private final ConfigLoader newPlaceholdersConfigLoader;
	private final Path oldPlaceholdersFile;
	private Config updatedConfig;

	public PlaceholdersUpgrade(ChestCommands plugin) {
		this.newPlaceholdersConfigLoader = plugin.getPlaceholdersConfigLoader();
		this.oldPlaceholdersFile = plugin.getDataPath("placeholders.yml");
	}

	@Override
	public Path getOriginalFile() {
		return oldPlaceholdersFile;
	}

	@Override
	public Path getUpgradedFile() {
		return newPlaceholdersConfigLoader.getPath();
	}

	@Override
	protected void computeChanges() throws UpgradeException {
		if (!Files.isRegularFile(oldPlaceholdersFile)) {
			return;
		}

		// Do NOT load the new placeholder configuration from disk, as it should only contain placeholders imported from the old file
		Config newPlaceholdersConfig = newPlaceholdersConfigLoader.loadEmpty();
		List<String> lines;
		try {
			lines = Files.readAllLines(oldPlaceholdersFile);
		} catch (IOException e) {
			throw new UpgradeException("couldn't read file \"" + oldPlaceholdersFile.getFileName() + "\"", e);
		}

		for (String line : lines) {
			// Comment or empty line
			if (line.isEmpty() || line.startsWith("#")) {
				continue;
			}

			// Ignore bad line
			if (!line.contains(":")) {
				continue;
			}

			String[] parts = Strings.trimmedSplit(line, ":", 2);
			String placeholder = unquote(parts[0]);
			String replacement = StringEscapeUtils.unescapeJava(unquote(parts[1]));

			newPlaceholdersConfig.set(placeholder, replacement);
			setModified();
		}

		this.updatedConfig = newPlaceholdersConfig;
	}

	@Override
	protected void saveChanges() throws IOException {
		try {
			Files.deleteIfExists(oldPlaceholdersFile);
		} catch (IOException ignored) {}
		newPlaceholdersConfigLoader.save(updatedConfig);
	}

	private static String unquote(String input) {
		if (input.length() < 2) {
			// Too short, cannot be a quoted string
			return input;
		}
		if (input.startsWith("'") && input.endsWith("'")) {
			return input.substring(1, input.length() - 1);
		} else if (input.startsWith("\"") && input.endsWith("\"")) {
			return input.substring(1, input.length() - 1);
		}

		return input;
	}

}
