/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.menu;

import me.filoghost.chestcommands.api.Icon;
import me.filoghost.chestcommands.api.Menu;
import me.filoghost.chestcommands.api.MenuView;
import me.filoghost.chestcommands.inventory.ArrayGrid;
import me.filoghost.chestcommands.inventory.DefaultMenuView;
import me.filoghost.chestcommands.inventory.Grid;
import me.filoghost.fcommons.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class BaseMenu implements Menu {

    
    private final String title;
    private final Grid<Icon> icons;


    public BaseMenu(String title, int rows) {
        Preconditions.notNull(title, "title");
        Preconditions.checkArgument(rows > 0, "rows must be greater than 0");
        this.title = title;
        this.icons = new ArrayGrid<>(rows, 9);
    }

    @Override
    public void setIcon(int row, int column, Icon icon) {
        icons.set(row, column, icon);
    }

    @Override
    public Icon getIcon(int row, int column) {
        return icons.get(row, column);
    }

    @Override
    public int getRowCount() {
        return icons.getRows();
    }
    
    @Override
    public int getColumnCount() {
        return icons.getColumns();
    }

    @Override
    public String getTitle() {
        return title;
    }

    public Grid<Icon> getIcons() {
        return icons;
    }

    @Override
    public MenuView open(Player player) {
        Preconditions.notNull(player, "player");

        DefaultMenuView menuView = new DefaultMenuView(this, player);
        menuView.open(player);
        return menuView;
    }

    @Override
    public void refreshMenuViews() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            DefaultMenuView menuView = MenuManager.getOpenMenuView(player);
            if (menuView != null && menuView.getMenu() == this) {
                menuView.refresh();
            }
        }
    }
}
