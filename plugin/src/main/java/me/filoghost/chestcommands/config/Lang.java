/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.config;

import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.fcommons.config.mapped.MappedConfig;
import me.filoghost.fcommons.config.mapped.modifier.ChatColors;

@ChatColors
public class Lang implements MappedConfig {

    public String no_open_permission = "&cYou don't have permission &e{permission} &cto use this menu.";
    public String default_no_icon_permission = "&cYou don't have permission for this icon.";
    public String no_required_item = "&cYou must have &e{amount}x {material} &c(durability: {durability}) for this.";
    public String no_money = "&cYou need {money}$ for this.";
    public String no_exp = "&cYou need {levels} XP levels for this.";
    public String menu_not_found = "&cMenu not found! " + Errors.User.notifyStaffRequest;
    public String any = "any"; // Used in no_required_item when durability is not restrictive
    
    private static Lang instance;
    
    static void setInstance(Lang instance) {
        Lang.instance = instance;
    }

    public static Lang get() {
        return instance;
    }
    
}
