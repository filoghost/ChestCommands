/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.menu;

import com.google.common.collect.ImmutableList;
import java.nio.file.Path;
import java.util.List;
import me.filoghost.chestcommands.Permissions;
import me.filoghost.chestcommands.action.Action;
import me.filoghost.chestcommands.api.MenuView;
import me.filoghost.chestcommands.config.Lang;
import me.filoghost.fcommons.collection.CollectionUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InternalMenu extends BaseMenu {

    private final Path sourceFile;
    private final String openPermission;

    private ImmutableList<Action> openActions;
    private int refreshTicks;
    private int autoCloseTicks;

    public InternalMenu(String title, int rows, Path sourceFile) {
        super(title, rows);
        this.sourceFile = sourceFile;
        this.openPermission = Permissions.OPEN_MENU_PREFIX + sourceFile.getFileName();
    }

    public Path getSourceFile() {
        return sourceFile;
    }

    public void setOpenActions(List<Action> openAction) {
        this.openActions = CollectionUtils.immutableCopy(openAction);
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

    public int getAutoCloseTicks()
    {
        return autoCloseTicks;
    }

    public void setAutoCloseTicks(int autoCloseTicks)
    {
        this.autoCloseTicks = autoCloseTicks;
    }

    @Override
    public MenuView open(Player player) {
        if (openActions != null) {
            for (Action openAction : openActions) {
                openAction.execute(player);
            }
        }

        return super.open(player);
    }

    public void openCheckingPermission(Player player) {
        if (player.hasPermission(openPermission)) {
            open(player);
        } else {
            sendNoOpenPermissionMessage(player);
        }
    }

    public void sendNoOpenPermissionMessage(CommandSender sender) {
        String noPermMessage = Lang.no_open_permission;
        if (noPermMessage != null && !noPermMessage.isEmpty()) {
            sender.sendMessage(noPermMessage.replace("{permission}", this.openPermission));
        }
    }

}
