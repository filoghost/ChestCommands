package com.gmail.filoghost.chestcommands.internal.icon;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.gmail.filoghost.chestcommands.config.AsciiPlaceholders;
import com.gmail.filoghost.chestcommands.internal.Variable;

public abstract class IconCommand {
	
	protected String command;
	private List<Variable> containedPlaceholders;
	
	public IconCommand(String command) {
		this.command = AsciiPlaceholders.placeholdersToSymbols(command).trim();
		this.containedPlaceholders = new ArrayList<Variable>();
		
		for (Variable placeholder : Variable.values()) {
			if (command.contains(placeholder.getText())) {
				containedPlaceholders.add(placeholder);
			}
		}
	}
	
	public String getParsedCommand(Player executor) {
		if (containedPlaceholders.isEmpty()) {
			return command;
		}
		
		String commandCopy = command;
		for (Variable placeholder : containedPlaceholders) {
			commandCopy = commandCopy.replace(placeholder.getText(), placeholder.getReplacement(executor));
		}
		return commandCopy;
	}
	
	public abstract void execute(Player player);
	
}
