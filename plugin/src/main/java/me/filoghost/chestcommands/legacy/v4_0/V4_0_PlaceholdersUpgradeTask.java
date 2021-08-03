/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy.v4_0;

import me.filoghost.chestcommands.config.ConfigManager;
import me.filoghost.chestcommands.legacy.upgrade.UpgradeTask;
import me.filoghost.fcommons.Strings;
import me.filoghost.fcommons.config.Config;
import me.filoghost.fcommons.config.ConfigErrors;
import me.filoghost.fcommons.config.ConfigLoader;
import me.filoghost.fcommons.config.exception.ConfigLoadException;
import me.filoghost.fcommons.config.exception.ConfigSaveException;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class V4_0_PlaceholdersUpgradeTask extends UpgradeTask {

    private final Path oldPlaceholdersFile;
    private final ConfigLoader newPlaceholdersConfigLoader;
    private Config updatedConfig;

    public V4_0_PlaceholdersUpgradeTask(ConfigManager configManager) {
        this.oldPlaceholdersFile = configManager.getRootDataFolder().resolve("placeholders.yml");
        this.newPlaceholdersConfigLoader = configManager.getConfigLoader("custom-placeholders.yml");
    }

    @Override
    public Path getOriginalFile() {
        return oldPlaceholdersFile;
    }

    @Override
    public Path getUpgradedFile() {
        return newPlaceholdersConfigLoader.getFile();
    }

    @Override
    public void computeChanges() throws ConfigLoadException {
        if (!Files.isRegularFile(getOriginalFile()) || Files.isRegularFile(getUpgradedFile())) {
            return;
        }

        // Do NOT load the new placeholder configuration from disk, as it should only contain placeholders imported from the old file
        Config newPlaceholdersConfig = new Config();
        List<String> lines;
        try {
            lines = Files.readAllLines(oldPlaceholdersFile);
        } catch (IOException e) {
            throw new ConfigLoadException(ConfigErrors.readIOException, e);
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

            String[] parts = Strings.splitAndTrim(line, ":", 2);
            String placeholder = unquote(parts[0]);
            String replacement = StringEscapeUtils.unescapeJava(unquote(parts[1]));

            newPlaceholdersConfig.setString("placeholders." + placeholder, replacement);
            setSaveRequired();
        }

        this.updatedConfig = newPlaceholdersConfig;
    }

    @Override
    public void saveChanges() throws ConfigSaveException {
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
