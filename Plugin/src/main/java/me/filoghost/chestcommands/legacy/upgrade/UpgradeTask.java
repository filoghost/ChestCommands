/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy.upgrade;

import me.filoghost.chestcommands.legacy.Backup;
import me.filoghost.chestcommands.logging.ErrorMessages;
import me.filoghost.commons.Preconditions;
import me.filoghost.commons.config.exception.ConfigLoadException;
import me.filoghost.commons.config.exception.ConfigSaveException;

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
