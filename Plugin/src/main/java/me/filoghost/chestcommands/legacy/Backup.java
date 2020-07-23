package me.filoghost.chestcommands.legacy;

import me.filoghost.chestcommands.util.Preconditions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Backup {

	private final Path dataFolder;
	private final Path backupFolder;

	public Backup(Path dataFolder, String backupName) {
		this.dataFolder = dataFolder;
		this.backupFolder = dataFolder.resolve("backups").resolve(backupName);
	}

	public static Backup newTimestampedBackup(Path dataFolder) {
		String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd-HH.mm"));
		String backupName = "backup_" + date;
		return new Backup(dataFolder, backupName);
	}

	public void backupFile(Path fileToBackup) throws IOException {
		Preconditions.checkArgument(fileToBackup.startsWith(dataFolder), "file is not inside data folder");
		Path destination = backupFolder.resolve(dataFolder.relativize(fileToBackup));
		Files.createDirectories(destination.getParent());
		if (!Files.isRegularFile(destination)) {
			Files.copy(fileToBackup, destination);
		}
	}

}
