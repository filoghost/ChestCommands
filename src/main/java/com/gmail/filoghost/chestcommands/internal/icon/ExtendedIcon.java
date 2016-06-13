package com.gmail.filoghost.chestcommands.internal.icon;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import com.gmail.filoghost.chestcommands.ChestCommands;
import com.gmail.filoghost.chestcommands.api.Icon;
import com.gmail.filoghost.chestcommands.bridge.EconomyBridge;
import com.gmail.filoghost.chestcommands.bridge.PlayerPointsBridge;
import com.gmail.filoghost.chestcommands.internal.ExtendedIconMenu;
import com.gmail.filoghost.chestcommands.internal.MenuInventoryHolder;
import com.gmail.filoghost.chestcommands.internal.RequiredItem;
import com.gmail.filoghost.chestcommands.util.StringUtils;
import com.gmail.filoghost.chestcommands.util.Utils;

public class ExtendedIcon extends Icon {

	private String permission;
	private String permissionMessage;
	private String viewPermission;
	
	private boolean permissionNegated;
	private boolean viewPermissionNegated;
	
	private double moneyPrice;
	private int playerPointsPrice;
	private int expLevelsPrice;
	private RequiredItem requiredItem;
	
	public ExtendedIcon() {
		super();
	}

	public boolean canClickIcon(Player player) {
		if (permission == null) {
			return true;
		}
		
		if (permissionNegated) {
			return !player.hasPermission(permission);
		} else {
			return player.hasPermission(permission);
		}
	}

	public void setPermission(String permission) {
		if (StringUtils.isNullOrEmpty(permission)) {
			permission = null;
		}
		
		if (permission != null) {
			if (permission.startsWith("-")) {
				permissionNegated = true;
				permission = permission.substring(1, permission.length()).trim();
			}
		}
		this.permission = permission;
	}

	public String getPermissionMessage() {
		return permissionMessage;
	}

	public void setPermissionMessage(String permissionMessage) {
		this.permissionMessage = permissionMessage;
	}
	
	public boolean hasViewPermission() {
		return viewPermission != null;
	}
	
	public boolean canViewIcon(Player player) {
		if (viewPermission == null) {
			return true;
		}
		
		if (viewPermissionNegated) {
			return !player.hasPermission(viewPermission);
		} else {
			return player.hasPermission(viewPermission);
		}
	}

	public void setViewPermission(String viewPermission) {
		if (StringUtils.isNullOrEmpty(viewPermission)) {
			viewPermission = null;
		}
		
		if (viewPermission != null) {
			if (viewPermission.startsWith("-")) {
				viewPermissionNegated = true;
				viewPermission = viewPermission.substring(1, viewPermission.length()).trim();
			}
		}
		this.viewPermission = viewPermission;
	}

	public double getMoneyPrice() {
		return moneyPrice;
	}

	public void setMoneyPrice(double moneyPrice) {
		this.moneyPrice = moneyPrice;
	}

	public int getPlayerPointsPrice() {
		return playerPointsPrice;
	}

	public void setPlayerPointsPrice(int playerPointsPrice) {
		this.playerPointsPrice = playerPointsPrice;
	}

	public int getExpLevelsPrice() {
		return expLevelsPrice;
	}

	public void setExpLevelsPrice(int expLevelsPrice) {
		this.expLevelsPrice = expLevelsPrice;
	}

	public RequiredItem getRequiredItem() {
		return requiredItem;
	}

	public void setRequiredItem(RequiredItem requiredItem) {
		this.requiredItem = requiredItem;
	}
	
	public String calculateName(Player pov) {
		return super.calculateName(pov);
	}
	
	public List<String> calculateLore(Player pov) {
		return super.calculateLore(pov);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onClick(Player player) {
		
		// Check all the requirements.
		
		if (!canClickIcon(player)) {
			if (permissionMessage != null) {
				player.sendMessage(permissionMessage);
			} else {
				player.sendMessage(ChestCommands.getLang().default_no_icon_permission);
			}
			return closeOnClick;
		}
		
		if (moneyPrice > 0) {
			if (!EconomyBridge.hasValidEconomy()) {
				player.sendMessage(ChatColor.RED + "This command has a price, but Vault with a compatible economy plugin was not found. For security, the command has been blocked. Please inform the staff.");
				return closeOnClick;
			}
			
			if (!EconomyBridge.hasMoney(player, moneyPrice)) {
				player.sendMessage(ChestCommands.getLang().no_money.replace("{money}", EconomyBridge.formatMoney(moneyPrice)));
				return closeOnClick;
			}
		}
		
		if (playerPointsPrice > 0) {
			if (!PlayerPointsBridge.hasValidPlugin()) {
				player.sendMessage(ChatColor.RED + "This command has a price in points, but the plugin PlayerPoints was not found. For security, the command has been blocked. Please inform the staff.");
				return closeOnClick;
			}
			
			if (!PlayerPointsBridge.hasPoints(player, playerPointsPrice)) {
				player.sendMessage(ChestCommands.getLang().no_points.replace("{points}", Integer.toString(playerPointsPrice)));
				return closeOnClick;
			}
		}
		
		if (expLevelsPrice > 0) {
			if (player.getLevel() < expLevelsPrice) {
				player.sendMessage(ChestCommands.getLang().no_exp.replace("{levels}", Integer.toString(expLevelsPrice)));
				return closeOnClick;
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
				return closeOnClick;
			}
		}
		
		// Take the money, the points and the required item.
		
		boolean changedVariables = false; // To update the placeholders.
		
		if (moneyPrice > 0) {
			if (!EconomyBridge.takeMoney(player, moneyPrice)) {
				player.sendMessage(ChatColor.RED + "Error: the transaction couldn't be executed. Please inform the staff.");
				return closeOnClick;
			}
			changedVariables = true;
		}
		
		if (playerPointsPrice > 0) {
			if (!PlayerPointsBridge.takePoints(player, playerPointsPrice)) {
				player.sendMessage(ChatColor.RED + "Error: the transaction couldn't be executed. Please inform the staff.");
				return closeOnClick;
			}
			changedVariables = true;
		}
		
		if (expLevelsPrice > 0) {
			player.setLevel(player.getLevel() - expLevelsPrice);
		}
		
		if (requiredItem != null) {
			requiredItem.takeItem(player);
		}
		
		if (changedVariables) {
			InventoryView view = player.getOpenInventory();
			if (view != null) {
				Inventory topInventory = view.getTopInventory();
				if (topInventory.getHolder() instanceof MenuInventoryHolder) {
					MenuInventoryHolder menuHolder = (MenuInventoryHolder) topInventory.getHolder();
					
					if (menuHolder.getIconMenu() instanceof ExtendedIconMenu) {
						((ExtendedIconMenu) menuHolder.getIconMenu()).refresh(player, topInventory);
					}
				}
			}
		}
		
		return super.onClick(player);
	}
	
	
}
