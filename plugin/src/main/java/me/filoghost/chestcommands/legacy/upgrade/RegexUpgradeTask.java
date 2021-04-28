/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy.upgrade;

import me.filoghost.fcommons.config.ConfigErrors;
import me.filoghost.fcommons.config.exception.ConfigLoadException;
import me.filoghost.fcommons.config.exception.ConfigSaveException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class RegexUpgradeTask extends UpgradeTask {

    private final Path file;
    private List<String> newContents;
    private Stream<String> linesStream;

    public RegexUpgradeTask(Path file) {
        this.file = file;
    }

    protected abstract void computeRegexChanges();

    @Override
    public final Path getOriginalFile() {
        return file;
    }

    @Override
    public final Path getUpgradedFile() {
        return file;
    }

    @Override
    public final void computeChanges() throws ConfigLoadException {
        if (!Files.isRegularFile(file)) {
            return;
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(file);
        } catch (IOException e) {
            throw new ConfigLoadException(ConfigErrors.readIOException, e);
        }

        this.linesStream = lines.stream();
        computeRegexChanges();

        newContents = linesStream.collect(Collectors.toList());

        if (!newContents.equals(lines)) {
            setSaveRequired();
        }
    }

    @Override
    public final void saveChanges() throws ConfigSaveException {
        try {
            Files.write(file, newContents);
        } catch (IOException e) {
            throw new ConfigSaveException(ConfigErrors.writeDataIOException, e);
        }
    }

    protected void renameInnerNode(String oldNode, String newNode) {
        replaceRegex(
                Pattern.compile("(^\\s+)" + Pattern.quote(oldNode) + "(:)"),
                matcher -> matcher.group(1) + newNode + matcher.group(2)
        );
    }

    protected void replaceRegex(Pattern regex, Function<Matcher, String> replaceCallback) {
        linesStream = linesStream.map(new RegexReplacer(regex, replaceCallback));
    }

}
