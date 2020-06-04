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
package me.filoghost.chestcommands.internal;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.filoghost.chestcommands.util.Validate;

public class RequiredItem {

	private Material material;
	private int amount;
	private short dataValue;
	private boolean isDurabilityRestrictive = false;

	public RequiredItem(Material material, int amount) {
		Validate.notNull(material, "Material cannot be null");
		Validate.isTrue(material != Material.AIR, "Material cannot be air");

		this.material = material;
		this.amount = amount;
	}

	public ItemStack createItemStack() {
		return new ItemStack(material, amount, dataValue);
	}

	public Material getMaterial() {
		return material;
	}

	public int getAmount() {
		return amount;
	}

	public short getDataValue() {
		return dataValue;
	}

	public void setRestrictiveDataValue(short data) {
		Validate.isTrue(data >= 0, "Data value cannot be negative");

		this.dataValue = data;
		isDurabilityRestrictive = true;
	}

	public boolean hasRestrictiveDataValue() {
		return isDurabilityRestrictive;
	}

	public boolean isValidDataValue(short data) {
		if (!isDurabilityRestrictive) return true;
		return data == this.dataValue;
	}

	public boolean hasItem(Player player) {
		int amountFound = 0;

		for (ItemStack item : player.getInventory().getContents()) {
			if (item != null && item.getType() == material && isValidDataValue(item.getDurability())) {
				amountFound += item.getAmount();
			}
		}

		return amountFound >= amount;
	}

	public boolean takeItem(Player player) {
		if (amount <= 0) {
			return true;
		}

		int itemsToTake = amount; //start from amount and decrease

		ItemStack[] contents = player.getInventory().getContents();
		ItemStack current = null;


		for (int i = 0; i < contents.length; i++) {

			current = contents[i];

			if (current != null && current.getType() == material && isValidDataValue(current.getDurability())) {
				if (current.getAmount() > itemsToTake) {
					current.setAmount(current.getAmount() - itemsToTake);
					return true;
				} else {
					itemsToTake -= current.getAmount();
					player.getInventory().setItem(i, new ItemStack(Material.AIR));
				}
			}

			// The end
			if (itemsToTake <= 0) return true;
		}

		return false;
	}
}
