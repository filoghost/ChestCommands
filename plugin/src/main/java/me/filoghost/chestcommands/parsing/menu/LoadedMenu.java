/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.parsing.menu;

import com.google.common.collect.ImmutableList;
import me.filoghost.chestcommands.menu.InternalMenu;
import me.filoghost.fcommons.collection.CollectionUtils;

import java.nio.file.Path;
import java.util.List;

public class LoadedMenu {

    private final InternalMenu menu;
    private final Path sourceFile;
    private final ImmutableList<String> openCommands;
    private final MenuOpenItem openItem;

    public LoadedMenu(InternalMenu menu, Path menuFile, List<String> openCommands, MenuOpenItem openItem) {
        this.menu = menu;
        this.sourceFile = menuFile;
        this.openCommands = CollectionUtils.newImmutableList(openCommands);
        this.openItem = openItem;
    }

    public InternalMenu getMenu() {
        return menu;
    }

    public Path getSourceFile() {
        return sourceFile;
    }

    public ImmutableList<String> getOpenCommands() {
        return openCommands;
    }

    public MenuOpenItem getOpenItem() {
        return openItem;
    }

}
