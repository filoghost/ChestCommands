package me.filoghost.chestcommands.config.framework.exception;

import org.bukkit.configuration.InvalidConfigurationException;

public class ConfigSyntaxException extends ConfigLoadException {

	public ConfigSyntaxException(String message, InvalidConfigurationException cause) {
		super(message, cause);
	}

}
