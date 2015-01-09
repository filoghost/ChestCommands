package com.gmail.filoghost.chestcommands.config;

import com.gmail.filoghost.chestcommands.config.yaml.PluginConfig;
import com.gmail.filoghost.chestcommands.config.yaml.SpecialConfig;

public class Settings extends SpecialConfig {
	
	public boolean use_console_colors = true;
	public String default_color__name = "&f";
	public String default_color__lore = "&7";
	public String multiple_commands_separator = ";";
	public boolean update_notifications = true;
	public int anti_click_spam_delay = 200;
	public boolean use_only_commands_without_args = true;

	public Settings(PluginConfig config) {
		super(config);
		setHeader("ChestCommands configuration file.\nTutorial: http://dev.bukkit.org/bukkit-plugins/chest-commands\n");
	}
	
}
