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
package me.filoghost.chestcommands.legacy.upgrade;

import me.filoghost.chestcommands.config.framework.exception.ConfigLoadException;
import me.filoghost.chestcommands.config.framework.exception.ConfigSaveException;
import me.filoghost.chestcommands.legacy.Backup;
import me.filoghost.chestcommands.logging.ErrorMessages;
import me.filoghost.chestcommands.util.Preconditions;

import java.io.IOException;
import java.nio.file.Path;

public abstract class UpgradeTask {

	private boolean saveRequired;
	private boolean hasRun;

	protected void setSaveRequired() {
		this.saveRequired = true;
	}

	public boolean runAndBackupIfNecessary(Backup backup) throws UpgradeTaskException {
		Preconditions.checkState(!hasRun, "Upgrade task has already run");
		hasRun = true;

		try {
			computeChanges();
		} catch (ConfigLoadException e) {
			throw new UpgradeTaskException(ErrorMessages.Upgrade.loadError(getOriginalFile()), e);
		}

		if (saveRequired) {
			try {
				backup.addFile(getOriginalFile());
			} catch (IOException e) {
				throw new UpgradeTaskException(ErrorMessages.Upgrade.backupError(getOriginalFile()), e);
			}

			try {
				saveChanges();
			} catch (ConfigSaveException e) {
				throw new UpgradeTaskException(ErrorMessages.Upgrade.saveError(getUpgradedFile()), e);
			}

			return true;

		} else {
			return false;
		}
	}

	public abstract Path getOriginalFile();

	public abstract Path getUpgradedFile();

	protected abstract void computeChanges() throws ConfigLoadException;

	protected abstract void saveChanges() throws ConfigSaveException;

}
