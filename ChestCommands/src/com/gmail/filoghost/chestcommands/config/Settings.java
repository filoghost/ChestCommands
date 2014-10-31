package com.gmail.filoghost.chestcommands.config;

import com.gmail.filoghost.chestcommands.config.yaml.PluginConfig;
import com.gmail.filoghost.chestcommands.config.yaml.SpecialConfig;

public class Settings extends SpecialConfig {
	
	public boolean update_notifications = true;
	public boolean use_console_colors = true;
	public String default_color__name = "&f";
	public String default_color__lore = "&7";

	public Settings(PluginConfig config) {
		super(config);
	}
	
}
