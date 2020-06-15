package me.filoghost.chestcommands.variable;

import org.bukkit.entity.Player;

import me.filoghost.chestcommands.hook.PlaceholderAPIHook;

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
		if (PlaceholderAPIHook.INSTANCE.isEnabled() && PlaceholderAPIHook.hasPlaceholders(message)) {
			return true;
		}
		return false;
	}

	public static String setVariables(String message, Player executor) {
		if (message == null) {
			return null;
		}
		for (Variable variable : Variable.values()) {
			if (message.contains(variable.getText())) {
				message = message.replace(variable.getText(), variable.getReplacement(executor));
			}
		}
		if (PlaceholderAPIHook.INSTANCE.isEnabled()) {
			message = PlaceholderAPIHook.setPlaceholders(message, executor);
		}
		return message;
	}

}
