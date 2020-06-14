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
package me.filoghost.chestcommands.menu.icon;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.filoghost.chestcommands.action.Action;
import me.filoghost.chestcommands.action.OpenMenuAction;
import me.filoghost.chestcommands.api.impl.ConfigurableIconImpl;
import me.filoghost.chestcommands.menu.AdvancedIconMenu;
import me.filoghost.chestcommands.menu.BaseIconMenu;
import me.filoghost.chestcommands.menu.MenuManager;
import me.filoghost.chestcommands.util.Utils;

public class AdvancedIcon extends ConfigurableIconImpl {

	private PermissionChecker viewPermissionChecker;

	private PermissionChecker requiredPermission;
	private RequiredMoney requiredMoney;
	private RequiredExpLevel requiredExpLevel;
	private RequiredItems requiredItems;
	private List<Action> clickActions;
	
	public boolean canViewIcon(Player player) {
		return viewPermissionChecker == null || viewPermissionChecker.hasPermission(player);
	}
	
	public boolean hasViewPermission() {
		return viewPermissionChecker != null && !viewPermissionChecker.isEmpty();
	}

	public void setClickPermission(String permission) {
		this.requiredPermission = new PermissionChecker(permission);
	}
	
	public void setNoClickPermissionMessage(String clickNoPermissionMessage) {
		requiredPermission.setNoPermissionMessage(clickNoPermissionMessage);
	}
		
	public void setViewPermission(String viewPermission) {
		this.viewPermissionChecker = new PermissionChecker(viewPermission);
	}

	public void setRequiredMoney(double requiredMoney) {
		if (requiredMoney > 0.0) {
			this.requiredMoney = new RequiredMoney(requiredMoney);
		} else {
			this.requiredMoney = null;
		}
	}

	public void setRequiredExpLevel(int requiredLevels) {
		if (requiredLevels > 0) {
			this.requiredExpLevel = new RequiredExpLevel(requiredLevels);
		} else {
			this.requiredExpLevel = null;
		}
	}

	public void setRequiredItems(List<RequiredItem> requiredItems) {
		if (!Utils.isNullOrEmpty(requiredItems)) {
			this.requiredItems = new RequiredItems(requiredItems);
		} else {
			this.requiredItems = null;
		}
	}

	public void setClickActions(List<Action> clickActions) {
		this.clickActions = Utils.nullableCopy(clickActions);
	}

	@Override
	public boolean onClick(Inventory inventory, Player player) {
		// Check all the requirements
		boolean hasAllRequirements = Requirement.checkAll(player, requiredPermission, requiredMoney, requiredExpLevel, requiredItems);
		if (!hasAllRequirements) {
			return closeOnClick;
		}

		// If all requirements are satisfied, take their cost
		boolean takenAllCosts = Requirement.takeCostAll(player, requiredPermission, requiredMoney, requiredExpLevel, requiredItems);
		if (!takenAllCosts) {
			return closeOnClick;
		}
		
		boolean hasOpenMenuAction = false;
		
		if (clickActions != null) {
			for (Action action : clickActions) {
				action.execute(player);
				
				if (action instanceof OpenMenuAction) {
	                hasOpenMenuAction = true;
	            }
			}
		}
		
		// Update the menu after taking requirement costs and executing all actions
        BaseIconMenu<?> menu = MenuManager.getOpenMenu(inventory);
        if (menu instanceof AdvancedIconMenu) {
            ((AdvancedIconMenu) menu).refresh(player, inventory);               
        }
		
		// Force menu to stay open if actions open another menu
		if (hasOpenMenuAction) {
			return false;
		} else {
		    return closeOnClick;
		}
	}


}
