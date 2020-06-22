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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class UpgradesDoneRegistry {

	private final Path saveFile;
	private final Set<String> upgradesDone;
	private boolean needSave;

	public UpgradesDoneRegistry(Path saveFile) throws IOException {
		this.saveFile = saveFile;
		this.upgradesDone = new HashSet<>();

		if (Files.isRegularFile(saveFile)) {
			Files.lines(saveFile, StandardCharsets.UTF_8).forEach(upgradesDone::add);
		}
	}

	public void runIfNecessary(Upgrade upgrade, UpgradeTask upgradeTask) throws ConversionException {
		if (!upgradesDone.contains(upgrade.propertyName)) {
			upgradeTask.run();
			setDone(upgrade);
		}
	}

	public void setAllDone() {
		for (Upgrade upgrade : Upgrade.values()) {
			setDone(upgrade);
		}
	}

	private void setDone(Upgrade upgrade) {
		if (upgradesDone.add(upgrade.propertyName)) {
			needSave = true;
		}
	}


	public void save() throws IOException {
		if (needSave) {
			Files.write(saveFile, upgradesDone, StandardCharsets.UTF_8);
			needSave = false;
		}
	}


	public interface UpgradeTask {

		void run() throws ConversionException;

	}


	public enum Upgrade {

		V4_MENUS("v4.0-menus"),
		V4_CONFIG("v4.0-config");

		private final String propertyName;

		Upgrade(String propertyName) {
			this.propertyName = propertyName;
		}
	}

}
