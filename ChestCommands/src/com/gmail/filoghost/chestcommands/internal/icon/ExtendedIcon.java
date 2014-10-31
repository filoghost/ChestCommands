package com.gmail.filoghost.chestcommands.internal.icon;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.gmail.filoghost.chestcommands.ChestCommands;
import com.gmail.filoghost.chestcommands.Permissions;
import com.gmail.filoghost.chestcommands.api.Icon;
import com.gmail.filoghost.chestcommands.bridge.EconomyBridge;
import com.gmail.filoghost.chestcommands.internal.RequiredItem;
import com.gmail.filoghost.chestcommands.util.Utils;

public class ExtendedIcon extends Icon {

	private String permission;
	private String permissionMessage;
	private double price;
	private RequiredItem requiredItem;
	
	public ExtendedIcon() {
		super();
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getPermissionMessage() {
		return permissionMessage;
	}

	public void setPermissionMessage(String permissionMessage) {
		this.permissionMessage = permissionMessage;
	}
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public RequiredItem getRequiredItem() {
		return requiredItem;
	}

	public void setRequiredItem(RequiredItem requiredItem) {
		this.requiredItem = requiredItem;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(Player player) {
		
		// Check all the requirements.
		
		if (permission != null && !permission.isEmpty() && !player.hasPermission(permission)) {
			if (permissionMessage != null) {
				player.sendMessage(permissionMessage);
			} else {
				player.sendMessage(ChatColor.RED + "You don't have permission.");
			}
			return;
		}
		
		if (price > 0) {
			if (!EconomyBridge.hasValidEconomy()) {
				player.sendMessage(ChatColor.RED + "This command has a price, but Vault with a compatible economy plugin was not found. For security, the command has been blocked. Please inform the staff.");
				return;
			}
			
			if (!player.hasPermission(Permissions.BYPASS_ECONOMY) && !EconomyBridge.hasMoney(player, price)) {
				player.sendMessage(ChestCommands.getLang().no_money.replace("{money}", EconomyBridge.formatMoney(price)));
				return;
			}
		}
		
		if (requiredItem != null) {
			
			if (!requiredItem.hasItem(player)) {
				player.sendMessage(ChestCommands.getLang().no_required_item
						.replace("{material}", Utils.formatMaterial(requiredItem.getMaterial()))
						.replace("{id}", Integer.toString(requiredItem.getMaterial().getId()))
						.replace("{amount}", Integer.toString(requiredItem.getAmount()))
						.replace("{datavalue}", requiredItem.hasRestrictiveDataValue() ? Short.toString(requiredItem.getDataValue()) : ChestCommands.getLang().any)
				);
				return;
			}
		}
		
		// Take the money and the required item.
		
		if (price > 0) {
			if (!player.hasPermission(Permissions.BYPASS_ECONOMY) && !EconomyBridge.takeMoney(player, price)) {
				player.sendMessage(ChatColor.RED + "Error: the transaction couldn't be executed. Please inform the staff.");
				return;
			}
		}
		
		if (requiredItem != null) {
			requiredItem.takeItem(player);
		}
		
		super.onClick(player);
	}
	
	
}
