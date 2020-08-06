/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.chestcommands.icon.requirement.item;

import com.google.common.collect.ImmutableList;
import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.icon.requirement.Requirement;
import me.filoghost.chestcommands.util.Utils;
import org.bukkit.entity.Player;

import java.util.List;

public class RequiredItems implements Requirement {

	private final List<RequiredItem> items;
	
	public RequiredItems(List<RequiredItem> items) {
		this.items = ImmutableList.copyOf(items);
	}

	@Override
	public boolean hasCost(Player player) {
		InventoryTakeHelper inventoryTakeHelper = new InventoryTakeHelper(player.getInventory());
		List<RequiredItem> missingItems = inventoryTakeHelper.prepareTakeItems(items);

		for (RequiredItem item : missingItems) {
			player.sendMessage(ChestCommands.getLang().no_required_item
					.replace("{material}", Utils.formatEnum(item.getMaterial()))
					.replace("{amount}", Integer.toString(item.getAmount()))
					.replace("{durability}", item.hasRestrictiveDurability() ? Short.toString(item.getDurability()) : ChestCommands.getLang().any)
			);
		}
		
		return missingItems.isEmpty();
	}

	@Override
	public boolean takeCost(Player player) {
		InventoryTakeHelper inventoryTakeHelper = new InventoryTakeHelper(player.getInventory());
		List<RequiredItem> missingItems = inventoryTakeHelper.prepareTakeItems(items);

		if (!missingItems.isEmpty()) {
			return false;
		}
		
		inventoryTakeHelper.applyTakeItems();
		return true;
	}
	
}
