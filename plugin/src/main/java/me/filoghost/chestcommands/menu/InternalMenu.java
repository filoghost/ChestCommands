/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.menu;

import com.google.common.collect.ImmutableList;
import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.Permissions;
import me.filoghost.chestcommands.action.Action;
import me.filoghost.chestcommands.api.MenuView;
import me.filoghost.chestcommands.config.Lang;
import me.filoghost.fcommons.collection.CollectionUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;

public class InternalMenu extends BaseMenu {

    private final Path sourceFile;
    private final String openPermission;

    private ImmutableList<Action> openActions;
    private int refreshTicks;

    public InternalMenu(@NotNull String title, int rows, @NotNull Path sourceFile) {
        super(title, rows);
        this.sourceFile = sourceFile;
        this.openPermission = Permissions.OPEN_MENU_PREFIX + sourceFile.getFileName();
    }

    public @NotNull Path getSourceFile() {
        return sourceFile;
    }

    public void setOpenActions(List<Action> openAction) {
        this.openActions = CollectionUtils.newImmutableList(openAction);
    }

    public String getOpenPermission() {
        return openPermission;
    }

    public int getRefreshTicks() {
        return refreshTicks;
    }

    public void setRefreshTicks(int refreshTicks) {
        this.refreshTicks = refreshTicks;
    }

    @Override
    public @NotNull MenuView open(@NotNull Player player) {
        if (openActions != null) {
            for (Action openAction : openActions) {
                openAction.execute(player);
            }
        }

        return super.open(player);
    }

    @Override
    public Plugin getPlugin() {
        return ChestCommands.getInstance();
    }

    public void openCheckingPermission(Player player) {
        if (player.hasPermission(openPermission)) {
            open(player);
        } else {
            sendNoOpenPermissionMessage(player);
        }
    }

    public void sendNoOpenPermissionMessage(CommandSender sender) {
        String noPermMessage = Lang.get().no_open_permission;
        if (noPermMessage != null && !noPermMessage.isEmpty()) {
            sender.sendMessage(noPermMessage.replace("{permission}", this.openPermission));
        }
    }

}
