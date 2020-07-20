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
package me.filoghost.chestcommands.icon;

import me.filoghost.chestcommands.action.Action;
import me.filoghost.chestcommands.action.OpenMenuAction;
import me.filoghost.chestcommands.api.ClickResult;
import me.filoghost.chestcommands.api.MenuInventory;
import me.filoghost.chestcommands.icon.requirement.PermissionChecker;
import me.filoghost.chestcommands.icon.requirement.RequiredExpLevel;
import me.filoghost.chestcommands.icon.requirement.RequiredItem;
import me.filoghost.chestcommands.icon.requirement.RequiredItems;
import me.filoghost.chestcommands.icon.requirement.RequiredMoney;
import me.filoghost.chestcommands.icon.requirement.Requirement;
import me.filoghost.chestcommands.util.Preconditions;
import me.filoghost.chestcommands.util.collection.CollectionUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class InternalConfigurableIcon extends BaseConfigurableIcon implements RefreshableIcon {

	private PermissionChecker viewPermission;

	private PermissionChecker clickPermission;
	private RequiredMoney requiredMoney;
	private RequiredExpLevel requiredExpLevel;
	private RequiredItems requiredItems;
	private List<Action> clickActions;

	private ClickResult clickResult;
	
	public InternalConfigurableIcon(Material material) {
		super(material);
		setPlaceholdersEnabled(true);
		this.clickResult = ClickResult.CLOSE;
	}

	public boolean canViewIcon(Player player) {
		return viewPermission == null || viewPermission.hasPermission(player);
	}
	
	public boolean hasViewPermission() {
		return viewPermission != null && !viewPermission.isEmpty();
	}

	public void setClickPermission(String permission) {
		this.clickPermission = new PermissionChecker(permission);
	}
	
	public void setNoClickPermissionMessage(String clickNoPermissionMessage) {
		clickPermission.setNoPermissionMessage(clickNoPermissionMessage);
	}
		
	public void setViewPermission(String viewPermission) {
		this.viewPermission = new PermissionChecker(viewPermission);
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
		if (!CollectionUtils.isNullOrEmpty(requiredItems)) {
			this.requiredItems = new RequiredItems(requiredItems);
		} else {
			this.requiredItems = null;
		}
	}

	public void setClickActions(List<Action> clickActions) {
		this.clickActions = CollectionUtils.nullableCopy(clickActions);
	}
	
	
	@Override
	public ItemStack render(Player viewer) {
		if (canViewIcon(viewer)) {
			return super.render(viewer);
		} else {
			return null;
		}
	}

	@Override
	protected boolean shouldCacheRendering() {
		return super.shouldCacheRendering() && !hasViewPermission();
	}


	public void setClickResult(ClickResult clickResult) {
		Preconditions.notNull(clickResult, "clickResult");
		this.clickResult = clickResult;
	}

	@Override
	public ClickResult onClick(MenuInventory menuInventory, Player player) {
		// Check all the requirements
		boolean hasAllRequirements = Requirement.hasAll(player, clickPermission, requiredMoney, requiredExpLevel, requiredItems);
		if (!hasAllRequirements) {
			return clickResult;
		}

		// If all requirements are satisfied, take their cost
		boolean takenAllCosts = Requirement.takeAllCosts(player, clickPermission, requiredMoney, requiredExpLevel, requiredItems);
		if (!takenAllCosts) {
			return clickResult;
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
		menuInventory.refresh();
		
		// Force menu to stay open if actions open another menu
		if (hasOpenMenuAction) {
			return ClickResult.KEEP_OPEN;
		} else {
		    return clickResult;
		}
	}

	@Override
	public ItemStack updateRendering(Player viewer, ItemStack currentRendering) {
		if (currentRendering != null && shouldCacheRendering()) {
			// Internal icons do not change, no need to update if the item is already rendered
			return currentRendering;
		}

		if (!canViewIcon(viewer)) {
			// Hide the current item
			return null;
		}

		if (currentRendering == null) {
			// Render item normally
			return render(viewer);
		} else {
			// Internal icons are loaded and then never change, we can safely update only name and lore (for performance)
			ItemMeta meta = currentRendering.getItemMeta();
			meta.setDisplayName(renderName(viewer));
			meta.setLore(renderLore(viewer));
			currentRendering.setItemMeta(meta);
			return currentRendering;
		}
	}

}
