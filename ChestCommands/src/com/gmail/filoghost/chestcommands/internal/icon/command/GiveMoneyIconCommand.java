package com.gmail.filoghost.chestcommands.internal.icon.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.gmail.filoghost.chestcommands.bridge.EconomyBridge;
import com.gmail.filoghost.chestcommands.internal.icon.IconCommand;
import com.gmail.filoghost.chestcommands.util.Utils;

public class GiveMoneyIconCommand extends IconCommand {
	
	private double moneyToGive;
	private String errorMessage;
	
	public GiveMoneyIconCommand(String command) {
		super(command);
		
		if (!Utils.isValidPositiveDouble(command)) {
			errorMessage = ChatColor.RED + "Invalid money amount: " + command;
			return;
		}
		
		moneyToGive = Double.parseDouble(command);
	}

	@Override
	public void execute(Player player) {
		if (errorMessage != null) {
			player.sendMessage(errorMessage);
			return;
		}
		
		if (EconomyBridge.hasValidEconomy()) {
			EconomyBridge.giveMoney(player, moneyToGive);
		} else {
			player.sendMessage(ChatColor.RED + "Vault with a compatible economy plugin not found. Please inform the staff.");
		}
	}

}
