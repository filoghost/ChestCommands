package com.gmail.filoghost.chestcommands.internal.icon.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.chestcommands.exception.FormatException;
import com.gmail.filoghost.chestcommands.internal.icon.IconCommand;
import com.gmail.filoghost.chestcommands.util.ItemStackReader;

public class GiveIconCommand extends IconCommand {
	
	private ItemStack itemToGive;
	private String errorMessage;
	
	public GiveIconCommand(String command) {
		super(command);
		
		try {
			ItemStackReader reader = new ItemStackReader(command, true);
			itemToGive = reader.createStack();	
			
		} catch (FormatException e) {
			errorMessage = ChatColor.RED + "Invalid item to give: " + e.getMessage();
		}
	}

	@Override
	public void execute(Player player) {
		if (errorMessage != null) {
			player.sendMessage(errorMessage);
			return;
		}
		
		player.getInventory().addItem(itemToGive);
	}

}
