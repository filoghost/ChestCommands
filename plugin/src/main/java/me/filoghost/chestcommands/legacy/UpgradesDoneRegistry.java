/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy;

import me.filoghost.chestcommands.legacy.upgrade.Upgrade;

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
        for (Upgrade upgrade : UpgradeList.getOrderedUpgrades()) {
            setDone(upgrade);
        }
    }

    public void setDone(Upgrade upgrade) {
        if (upgradesDone.add(upgrade.getID())) {
            needSave = true;
        }
    }

    public boolean isDone(Upgrade upgrade) {
        return upgradesDone.contains(upgrade.getID());
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

}
