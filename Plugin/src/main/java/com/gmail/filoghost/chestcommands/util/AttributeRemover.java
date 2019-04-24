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
package com.gmail.filoghost.chestcommands.util;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AttributeRemover {

	public static ItemStack hideAttributes(ItemStack item) {
		if (item == null) {
			return null;
		}

		ItemMeta meta = item.getItemMeta();
		if (Utils.isNullOrEmpty(meta.getItemFlags())) {
			// Add them only if necessary
			meta.addItemFlags(ItemFlag.values());
			item.setItemMeta(meta);
		}
		return item;
	}

}
