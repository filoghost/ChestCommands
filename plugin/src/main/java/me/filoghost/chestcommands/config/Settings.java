/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.config;

import me.filoghost.fcommons.config.mapped.IncludeStatic;
import me.filoghost.fcommons.config.mapped.MappedConfig;
import me.filoghost.fcommons.config.mapped.modifier.ChatColors;

@ChatColors
@IncludeStatic
public class Settings extends MappedConfig {

    public static String default_color__name = "&f";
    public static String default_color__lore = "&7";
    public static boolean update_notifications = true;
    public static int anti_click_spam_delay = 200;

    public Settings() {
        setHeader(
                "ChestCommands main configuration file.",
                "Documentation: https://filoghost.me/docs/chest-commands");
    }

}
