/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.parsing.menu;

import com.google.common.collect.ImmutableList;
import me.filoghost.chestcommands.action.Action;
import me.filoghost.fcommons.collection.CollectionUtils;

import java.util.List;

public class MenuSettings {

    // Required settings
    private final String title;
    private final int rows;

    // Optional settings
    private ImmutableList<String> commands;
    private ImmutableList<Action> openActions;
    private int refreshTicks;

    private MenuOpenItem openItem;

    public MenuSettings(String title, int rows) {
        this.title = title;
        this.rows = rows;
    }

    public String getTitle() {
        return title;
    }

    public int getRows() {
        return rows;
    }

    public void setCommands(List<String> commands) {
        this.commands = CollectionUtils.newImmutableList(commands);
    }

    public ImmutableList<String> getCommands() {
        return commands;
    }

    public ImmutableList<Action> getOpenActions() {
        return openActions;
    }

    public void setOpenActions(List<Action> openAction) {
        this.openActions = CollectionUtils.newImmutableList(openAction);
    }

    public int getRefreshTicks() {
        return refreshTicks;
    }

    public void setRefreshTicks(int refreshTicks) {
        this.refreshTicks = refreshTicks;
    }

    public MenuOpenItem getOpenItem() {
        return openItem;
    }

    public void setOpenItem(MenuOpenItem openItem) {
        this.openItem = openItem;
    }

}
