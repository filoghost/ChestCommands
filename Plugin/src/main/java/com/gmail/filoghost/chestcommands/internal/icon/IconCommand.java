package com.gmail.filoghost.chestcommands.internal.icon;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.gmail.filoghost.chestcommands.config.AsciiPlaceholders;
import com.gmail.filoghost.chestcommands.internal.Variable;

public abstract class IconCommand {
	
	protected String command;
	private List<Variable> containedVariables;
	
	public IconCommand(String command) {
		this.command = AsciiPlaceholders.placeholdersToSymbols(command).trim();
		this.containedVariables = new ArrayList<Variable>();
		
		for (Variable variable : Variable.values()) {
			if (command.contains(variable.getText())) {
				containedVariables.add(variable);
			}
		}
	}
	
	public String getParsedCommand(Player executor) {
		if (containedVariables.isEmpty()) {
			return command;
		}
		
		String commandCopy = command;
		for (Variable variable : containedVariables) {
			commandCopy = commandCopy.replace(variable.getText(), variable.getReplacement(executor));
		}
		return commandCopy;
	}
	
	public abstract void execute(Player player);
	
}
