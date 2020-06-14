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

import com.google.common.collect.ImmutableList;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.util.MaterialsHelper;
import me.filoghost.chestcommands.util.Preconditions;

public class RequiredItems implements Requirement {

	private final List<RequiredItem> items;
	
	public RequiredItems(List<RequiredItem> items) {
		Preconditions.notEmpty(items, "items");
		this.items = ImmutableList.copyOf(items);
	}
	
	public List<RequiredItem> geItems() {
		return items;
	}	

	@Override
	public boolean hasRequirement(Player player) {
		boolean missingItems = false;
		
		for (RequiredItem item : items) {
			if (!item.isItemContainedIn(player.getInventory())) {
				missingItems = true;
				player.sendMessage(ChestCommands.getLang().no_required_item
						.replace("{material}", MaterialsHelper.formatMaterial(item.getMaterial()))
						.replace("{amount}", Integer.toString(item.getAmount()))
						.replace("{datavalue}", item.hasRestrictiveDurability() ? Short.toString(item.getDurability()) : ChestCommands.getLang().any)
				);
			}
		}
		
		return !missingItems;
	}

	@Override
	public boolean takeCost(Player player) {
		boolean missingItems = false;
		
		for (RequiredItem item : items) {
			boolean success = item.takeItemFrom(player.getInventory());
			if (!success) {
				missingItems = true;
			}
		}
		
		return !missingItems;
	}
	
}