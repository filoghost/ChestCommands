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
package me.filoghost.chestcommands.legacy;

import me.filoghost.chestcommands.config.yaml.Config;
import me.filoghost.chestcommands.config.yaml.ConfigLoader;
import me.filoghost.chestcommands.util.Preconditions;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Upgrade {

	private boolean modified;
	private boolean hasRun;

	protected void setModified() {
		this.modified = true;
	}


	public boolean backupAndUpgradeIfNecessary() throws UpgradeException {
		Preconditions.checkState(!hasRun, "Upgrade can only be run once");
		hasRun = true;

		computeChanges();

		if (modified) {
			try {
				createBackupFile(getOriginalFile());
			} catch (IOException e) {
				throw new UpgradeException("couldn't create backup of file \"" + getOriginalFile().getFileName() + "\"", e);
			}

			try {
				saveChanges();
			} catch (IOException e) {
				throw new UpgradeException("couldn't save upgraded file \"" + getUpgradedFile().getFileName() + "\"", e);
			}
		}

		return modified;
	}

	protected Config loadConfig(ConfigLoader configLoader) throws UpgradeException {
		try {
			return configLoader.load();
		} catch (IOException e) {
			throw new UpgradeException("couldn't read configuration file \"" + configLoader.getFileName() + "\"", e);
		} catch (InvalidConfigurationException e) {
			throw new UpgradeException("couldn't parse YAML syntax of file \"" + configLoader.getFileName() + "\"", e);
		}
	}

	private void createBackupFile(Path path) throws IOException {
		String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd-HH.mm"));
		String backupName = path.getFileName() + "_" + date + ".backup";

		Files.copy(path, path.resolveSibling(backupName), StandardCopyOption.REPLACE_EXISTING);
	}

	public abstract Path getOriginalFile();

	public abstract Path getUpgradedFile();

	protected abstract void computeChanges() throws UpgradeException;

	protected abstract void saveChanges() throws IOException;

}
