/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.action;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.chestcommands.menu.InternalMenu;
import me.filoghost.chestcommands.menu.MenuManager;
import me.filoghost.chestcommands.placeholder.PlaceholderString;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class OpenMenuAction implements Action {

    private final PlaceholderString targetMenu;
    
    public OpenMenuAction(String serializedAction) {
        targetMenu = PlaceholderString.of(serializedAction);
    }

    @Override
    public void execute(final Player player) {
        String menuName = targetMenu.getValue(player);
        final InternalMenu menu = MenuManager.getMenuByFileName(menuName);
        
        if (menu != null) {
            /*
             * Delay the task, since this action is executed in ClickInventoryEvent
             * and opening another inventory in the same moment is not a good idea.
             */
            Bukkit.getScheduler().runTask(ChestCommands.getInstance(), () -> {
                menu.openCheckingPermission(player);
            });

        } else {
            player.sendMessage(Errors.User.configurationError("couldn't find the menu \"" + menuName + "\""));
        }
    }

}
