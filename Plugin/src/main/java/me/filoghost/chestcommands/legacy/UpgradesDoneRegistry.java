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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class UpgradesDoneRegistry {

	private final Path saveFile;
	private final Set<String> upgradesDone;
	private boolean needSave;

	public UpgradesDoneRegistry(Path saveFile) throws IOException {
		this.saveFile = saveFile;
		this.upgradesDone = new HashSet<>();

		if (Files.isRegularFile(saveFile)) {
			try (Stream<String> lines = Files.lines(saveFile)) {
				lines.filter(s -> !s.startsWith("#"))
						.forEach(upgradesDone::add);
			}
		}
	}

	public void setAllDone() {
		for (UpgradeID upgrade : UpgradeID.values()) {
			setDone(upgrade);
		}
	}

	public void setDone(UpgradeID upgrade) {
		if (upgradesDone.add(upgrade.stringID)) {
			needSave = true;
		}
	}

	public boolean isDone(UpgradeID upgrade) {
		return upgradesDone.contains(upgrade.stringID);
	}

	public void save() throws IOException {
		if (needSave) {
			List<String> lines = new ArrayList<>();
			lines.add("#");
			lines.add("# WARNING: manually editing this file is not recommended");
			lines.add("#");
			lines.addAll(upgradesDone);
			Files.createDirectories(saveFile.getParent());
			Files.write(saveFile, lines);
			needSave = false;
		}
	}


	public enum UpgradeID {

		V4_MENUS("v4.0-menus"),
		V4_CONFIG("v4.0-config"),
		V4_PLACEHOLDERS("v4.0-placeholders");

		private final String stringID;

		UpgradeID(String stringID) {
			this.stringID = stringID;
		}
	}

}
