package me.filoghost.chestcommands.internal;

import org.bukkit.entity.Player;

import me.filoghost.chestcommands.bridge.PlaceholderAPIBridge;

public class VariableManager {

	public static boolean hasVariables(String message) {
		if(message == null) {
			return false;
		}
		for (Variable variable : Variable.values()) {
			if (message.contains(variable.getText())) {
				return true;
			}
		}
		if (PlaceholderAPIBridge.hasValidPlugin() && PlaceholderAPIBridge.hasPlaceholders(message)) {
			return true;
		}
		return false;
	}

	public static String setVariables(String message, Player executor) {
		for (Variable variable : Variable.values()) {
			if (message.contains(variable.getText())) {
				message = message.replace(variable.getText(), variable.getReplacement(executor));
			}
		}
		if (PlaceholderAPIBridge.hasValidPlugin()) {
			message = PlaceholderAPIBridge.setPlaceholders(message, executor);
		}
		return message;
	}

}
