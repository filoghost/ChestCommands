/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.parsing.menu;

import me.filoghost.fcommons.config.ConfigPath;

public class MenuSettingsPath {
    
    public static final ConfigPath ROOT_SECTION = ConfigPath.literal("menu-settings");

    public static final ConfigPath NAME = ConfigPath.literal("name");
    public static final ConfigPath ROWS = ConfigPath.literal("rows");

    public static final ConfigPath COMMANDS = ConfigPath.literal("commands");
    public static final ConfigPath OPEN_ACTIONS = ConfigPath.literal("open-actions");
    public static final ConfigPath AUTO_REFRESH = ConfigPath.literal("auto-refresh");

    public static final ConfigPath OPEN_ITEM_MATERIAL = ConfigPath.literal("open-with-item", "material");
    public static final ConfigPath OPEN_ITEM_LEFT_CLICK = ConfigPath.literal("open-with-item", "left-click");
    public static final ConfigPath OPEN_ITEM_RIGHT_CLICK = ConfigPath.literal("open-with-item", "right-click");

}
