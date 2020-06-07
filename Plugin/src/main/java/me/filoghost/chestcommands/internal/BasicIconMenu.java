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

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.filoghost.chestcommands.api.Icon;
import me.filoghost.chestcommands.api.IconMenu;
import me.filoghost.chestcommands.util.Preconditions;
import me.filoghost.chestcommands.util.Utils;

/*
 *    MEMO: Raw slot numbers
 *
 *    | 0| 1| 2| 3| 4| 5| 6| 7| 8|
 *    | 9|10|11|12|13|14|15|16|17|
 *    ...
 *
 */
public class BasicIconMenu implements IconMenu {

	protected final String title;
	protected final Icon[] icons;


	public BasicIconMenu(String title, int rows) {
		this.title = title;
		icons = new BasicIcon[rows * 9];
	}

	@Override
	public void setIcon(int x, int y, Icon icon) {
		int slot = Utils.makePositive(y - 1) * 9 + Utils.makePositive(x - 1);
		if (slot >= 0 && slot < icons.length) {
			icons[slot] = icon;
		}
	}

	@Override
	public void setIconRaw(int slot, Icon icon) {
		if (slot >= 0 && slot < icons.length) {
			icons[slot] = icon;
		}
	}

	@Override
	public Icon getIcon(int x, int y) {
		int slot = Utils.makePositive(y - 1) * 9 + Utils.makePositive(x - 1);
		if (slot >= 0 && slot < icons.length) {
			return icons[slot];
		}

		return null;
	}

	@Override
	public Icon getIconRaw(int slot) {
		if (slot >= 0 && slot < icons.length) {
			return icons[slot];
		}

		return null;
	}

	@Override
	public int getRows() {
		return icons.length / 9;
	}

	@Override
	public int getSize() {
		return icons.length;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void open(Player player) {
		Preconditions.notNull(player, "player");

		Inventory inventory = Bukkit.createInventory(new MenuInventoryHolder(this), icons.length, title);

		for (int i = 0; i < icons.length; i++) {
			if (icons[i] != null) {
				inventory.setItem(i, hideAttributes(icons[i].createItemstack(player)));
			}
		}

		player.openInventory(inventory);
	}
	
	protected ItemStack hideAttributes(ItemStack item) {
		if (item == null) {
			return null;
		}

		ItemMeta meta = item.getItemMeta();
		if (Utils.isNullOrEmpty(meta.getItemFlags())) {
			// Add them only if no flag was already set
			meta.addItemFlags(ItemFlag.values());
			item.setItemMeta(meta);
		}
		return item;
	}

	@Override
	public String toString() {
		return "IconMenu [title=" + title + ", icons=" + Arrays.toString(icons) + "]";
	}
}
