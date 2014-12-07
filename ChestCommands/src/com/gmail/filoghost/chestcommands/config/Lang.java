package com.gmail.filoghost.chestcommands.config;

import com.gmail.filoghost.chestcommands.config.yaml.PluginConfig;
import com.gmail.filoghost.chestcommands.config.yaml.SpecialConfig;

public class Lang extends SpecialConfig {
	
	public String no_open_permission = "&cYou don't have permission &e{permission} &cto use this menu.";
	public String default_no_icon_permission = "&cYou don't have permission for this icon.";
	public String no_required_item = "&cYou must have &e{amount}x {material} &c(ID: {id}, data value: {datavalue}) for this.";
	public String no_money = "&cYou need {money}$ for this.";
	public String no_points = "&cYou need {points} player points for this.";
	public String no_exp = "&cYou need {levels} XP levels for this.";
	public String menu_not_found = "&cMenu not found! Please inform the staff.";
	public String open_menu = "&aOpening the menu \"{menu}\".";
	public String open_menu_others = "&aOpening the menu \"{menu}\" to {player}.";
	public String any = "any"; // Used in no_required_item when data value is not restrictive.

	public Lang(PluginConfig config) {
		super(config);
	}
}
