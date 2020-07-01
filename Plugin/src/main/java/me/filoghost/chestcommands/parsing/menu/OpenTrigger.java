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
package me.filoghost.chestcommands.parsing.menu;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import me.filoghost.chestcommands.util.Preconditions;

public class OpenTrigger {

	private final Material material;
	private final ClickType clickType;
	private short durability;
	private boolean isRestrictiveDurability;

	public OpenTrigger(Material material, ClickType clickType) {
		Preconditions.checkArgumentNotAir(material, "material");
		Preconditions.notNull(clickType, "clickType");
		
		this.material = material;
		this.clickType = clickType;
	}

	public void setRestrictiveDurability(short durability) {
		this.durability = durability;
		this.isRestrictiveDurability = true;
	}

	public boolean matches(ItemStack item, Action action) {
		if (item == null) {
			return false;
		}

		if (this.material != item.getType()) {
			return false;
		}
		
		if (isRestrictiveDurability && this.durability != item.getDurability()) {
			return false;
		}

		return clickType.isValidInteract(action);
	}
}
