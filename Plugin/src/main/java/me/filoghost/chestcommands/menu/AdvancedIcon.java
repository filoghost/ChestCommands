/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.chestcommands.menu;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.action.Action;
import me.filoghost.chestcommands.action.GiveMoneyAction;
import me.filoghost.chestcommands.action.OpenMenuAction;
import me.filoghost.chestcommands.api.impl.ConfigurableIconImpl;
import me.filoghost.chestcommands.bridge.EconomyBridge;
import me.filoghost.chestcommands.util.MaterialsHelper;

public class AdvancedIcon extends ConfigurableIconImpl {

	private PermissionChecker clickPermissionChecker;
	private String clickNoPermissionMessage;
	
	private PermissionChecker viewPermissionChecker;

	private double moneyPrice;
	private int expLevelsPrice;
	private List<RequiredItem> requiredItems;
	private List<Action> clickActions;
	
	private boolean canClickIcon(Player player) {
		return clickPermissionChecker == null || clickPermissionChecker.hasPermission(player);
	}
	
	public boolean canViewIcon(Player player) {
		return viewPermissionChecker == null || viewPermissionChecker.hasPermission(player);
	}
	
	public boolean hasViewPermission() {
		return viewPermissionChecker != null && !viewPermissionChecker.isEmpty();
	}

	public void setPermission(String permission) {
		clickPermissionChecker = new PermissionChecker(permission);
	}

	public String getPermissionMessage() {
		return clickNoPermissionMessage;
	}

	public void setPermissionMessage(String clickNoPermissionMessage) {
		this.clickNoPermissionMessage = clickNoPermissionMessage;
	}
		
	public void setViewPermission(String viewPermission) {
		viewPermissionChecker = new PermissionChecker(viewPermission);
	}

	public double getMoneyPrice() {
		return moneyPrice;
	}

	public void setMoneyPrice(double moneyPrice) {
		this.moneyPrice = moneyPrice;
	}

	public int getExpLevelsPrice() {
		return expLevelsPrice;
	}

	public void setExpLevelsPrice(int expLevelsPrice) {
		this.expLevelsPrice = expLevelsPrice;
	}

	public List<RequiredItem> getRequiredItems() {
		return requiredItems;
	}

	public void setRequiredItems(List<RequiredItem> requiredItems) {
		this.requiredItems = requiredItems;
	}

	public List<Action> getClickActions() {
		return clickActions;
	}

	public void setClickActions(List<Action> clickActions) {
		this.clickActions = clickActions;
	}

	@Override
	public boolean onClick(Inventory inventory, Player player) {

		// Check all the requirements

		if (!canClickIcon(player)) {
			if (clickNoPermissionMessage != null) {
				player.sendMessage(clickNoPermissionMessage);
			} else {
				player.sendMessage(ChestCommands.getLang().default_no_icon_permission);
			}
			return closeOnClick;
		}

		if (moneyPrice > 0) {
			if (!EconomyBridge.hasValidEconomy()) {
				player.sendMessage(ChatColor.RED + "This action has a price, but Vault with a compatible economy plugin was not found. For security, the action has been blocked. Please inform the staff.");
				return closeOnClick;
			}

			if (!EconomyBridge.hasMoney(player, moneyPrice)) {
				player.sendMessage(ChestCommands.getLang().no_money.replace("{money}", EconomyBridge.formatMoney(moneyPrice)));
				return closeOnClick;
			}
		}

		if (expLevelsPrice > 0) {
			if (player.getLevel() < expLevelsPrice) {
				player.sendMessage(ChestCommands.getLang().no_exp.replace("{levels}", Integer.toString(expLevelsPrice)));
				return closeOnClick;
			}
		}

		if (requiredItems != null) {
			boolean notHasItem = false;
			for (RequiredItem item : requiredItems) {
				if (!item.isItemContainedIn(player.getInventory())) {
					notHasItem = true;
					player.sendMessage(ChestCommands.getLang().no_required_item
							.replace("{material}", MaterialsHelper.formatMaterial(item.getMaterial()))
							.replace("{amount}", Integer.toString(item.getAmount()))
							.replace("{datavalue}", item.hasRestrictiveDurability() ? Short.toString(item.getDurability()) : ChestCommands.getLang().any)
					);
				}
			}
			if (notHasItem) {
				return closeOnClick;
			}
		}

		// Take the money and the required item

		boolean changedVariables = false; // To update the placeholders

		if (moneyPrice > 0) {
			if (!EconomyBridge.takeMoney(player, moneyPrice)) {
				player.sendMessage(ChatColor.RED + "Error: the transaction couldn't be executed. Please inform the staff.");
				return closeOnClick;
			}
			changedVariables = true;
		}

		if (expLevelsPrice > 0) {
			player.setLevel(player.getLevel() - expLevelsPrice);
		}

		if (requiredItems != null) {
			for (RequiredItem item : requiredItems) {
				item.takeItemFrom(player.getInventory());
			}
		}
		
		boolean hasOpenMenuAction = false;
		
		if (clickActions != null) {
			for (Action action : clickActions) {
				action.execute(player);
				
				if (action instanceof OpenMenuAction) {
	                hasOpenMenuAction = true;
	            } else if (action instanceof GiveMoneyAction) {
	                changedVariables = true;
	            }
			}
		}
		
		if (changedVariables) {
            BaseIconMenu<?> menu = MenuManager.getOpenMenu(inventory);
            if (menu instanceof AdvancedIconMenu) {
                ((AdvancedIconMenu) menu).refresh(player, inventory);               
            }
        }
		
		// Force menu to stay open if actions open another menu
		if (hasOpenMenuAction) {
			return false;
		} else {
		    return closeOnClick;
		}
	}


}
