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
package me.filoghost.chestcommands.icon.requirement;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.filoghost.chestcommands.util.Preconditions;

public class RequiredItem {

	private final Material material;
	private final int amount;
	private short durability;
	private boolean isDurabilityRestrictive = false;

	public RequiredItem(Material material, int amount) {
		Preconditions.checkArgumentNotAir(material, "material");

		this.material = material;
		this.amount = amount;
	}

	public Material getMaterial() {
		return material;
	}

	public int getAmount() {
		return amount;
	}

	public short getDurability() {
		return durability;
	}

	public void setRestrictiveDurability(short durability) {
		Preconditions.checkArgument(durability >= 0, "Durability cannot be negative");

		this.durability = durability;
		isDurabilityRestrictive = true;
	}

	public boolean hasRestrictiveDurability() {
		return isDurabilityRestrictive;
	}

	public boolean isItemContainedIn(Inventory inventory) {
		int amountFound = 0;

		for (ItemStack item : inventory.getContents()) {
			if (isMatchingType(item)) {
				amountFound += item.getAmount();
			}
		}

		return amountFound >= amount;
	}

	public boolean takeItemFrom(Inventory inventory) {
		if (amount <= 0) {
			return true;
		}
		
		int itemsToTake = amount; // Start from amount and decrease
		ItemStack[] contents = inventory.getContents();

		for (int i = 0; i < contents.length; i++) {
			ItemStack current = contents[i];

			if (isMatchingType(current)) {
				if (current.getAmount() > itemsToTake) {
					current.setAmount(current.getAmount() - itemsToTake);
					return true;
				} else {
					itemsToTake -= current.getAmount();
					inventory.setItem(i, new ItemStack(Material.AIR));
				}
			}

			// The end
			if (itemsToTake <= 0) return true;
		}

		return false;
	}
	
	private boolean isMatchingType(ItemStack item) {
		return item != null && item.getType() == material && isMatchingDurability(item.getDurability());
	}
	
	private boolean isMatchingDurability(short durability) {
		if (!isDurabilityRestrictive) {
			return true;
		}
		return this.durability == durability;
	}
	
}
