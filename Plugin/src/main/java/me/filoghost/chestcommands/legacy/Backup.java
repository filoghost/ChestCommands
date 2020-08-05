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
package me.filoghost.chestcommands.legacy;

import me.filoghost.chestcommands.util.Preconditions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Backup {

	private final Path dataFolder;
	private final Path backupFolder;
	private final Path infoFile;

	public Backup(Path dataFolder, String backupName) {
		this.dataFolder = dataFolder;
		Path backupsFolder = dataFolder.resolve("old_files");
		this.backupFolder = backupsFolder.resolve(backupName);
		this.infoFile = backupsFolder.resolve("readme.txt");
	}

	public static Backup newTimestampedBackup(Path dataFolder) {
		String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd-HH.mm"));
		String backupName = "backup_" + date;
		return new Backup(dataFolder, backupName);
	}

	public void addFile(Path fileToBackup) throws IOException {
		Preconditions.checkArgument(fileToBackup.startsWith(dataFolder), "file is not inside data folder");
		Path destination = backupFolder.resolve(dataFolder.relativize(fileToBackup));
		Files.createDirectories(destination.getParent());
		if (!Files.isRegularFile(destination)) {
			Files.copy(fileToBackup, destination);
		}
		if (!Files.isRegularFile(infoFile)) {
			Files.write(infoFile, Arrays.asList(
					"Files in this folders are copies of original configuration files that have been automatically upgraded.",
					"",
					"Note: some configuration upgrades remove comments and other formatting (such as empty lines)."
			));
		}
	}

}
