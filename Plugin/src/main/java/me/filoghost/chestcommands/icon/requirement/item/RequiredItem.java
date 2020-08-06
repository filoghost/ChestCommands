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

import me.filoghost.chestcommands.util.Preconditions;
import org.bukkit.Material;

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

	public boolean isMatchingType(RemainingItem item) {
		return item != null && item.getMaterial() == material && isMatchingDurability(item.getDurability());
	}
	
	private boolean isMatchingDurability(short durability) {
		if (isDurabilityRestrictive) {
			return this.durability == durability;
		} else {
			return true;
		}
	}
	
}
