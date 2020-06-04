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
package me.filoghost.chestcommands.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.filoghost.chestcommands.internal.MenuInventoryHolder;
import me.filoghost.chestcommands.util.ItemUtils;
import me.filoghost.chestcommands.util.Utils;
import me.filoghost.chestcommands.util.Validate;

import java.util.Arrays;

/*
 *    MEMO: Raw slot numbers
 *
 *    | 0| 1| 2| 3| 4| 5| 6| 7| 8|
 *    | 9|10|11|12|13|14|15|16|17|
 *    ...
 *
 */
public class IconMenu {

	protected final String title;
	protected final Icon[] icons;


	public IconMenu(String title, int rows) {
		this.title = title;
		icons = new Icon[rows * 9];
	}

	public void setIcon(int x, int y, Icon icon) {
		int slot = Utils.makePositive(y - 1) * 9 + Utils.makePositive(x - 1);
		if (slot >= 0 && slot < icons.length) {
			icons[slot] = icon;
		}
	}

	public void setIconRaw(int slot, Icon icon) {
		if (slot >= 0 && slot < icons.length) {
			icons[slot] = icon;
		}
	}

	public Icon getIcon(int x, int y) {
		int slot = Utils.makePositive(y - 1) * 9 + Utils.makePositive(x - 1);
		if (slot >= 0 && slot < icons.length) {
			return icons[slot];
		}

		return null;
	}

	public Icon getIconRaw(int slot) {
		if (slot >= 0 && slot < icons.length) {
			return icons[slot];
		}

		return null;
	}

	public int getRows() {
		return icons.length / 9;
	}

	public int getSize() {
		return icons.length;
	}

	public String getTitle() {
		return title;
	}

	public void open(Player player) {
		Validate.notNull(player, "Player cannot be null");

		Inventory inventory = Bukkit.createInventory(new MenuInventoryHolder(this), icons.length, title);

		for (int i = 0; i < icons.length; i++) {
			if (icons[i] != null) {
				inventory.setItem(i, ItemUtils.hideAttributes(icons[i].createItemstack(player)));
			}
		}

		player.openInventory(inventory);
	}

	@Override
	public String toString() {
		return "IconMenu [title=" + title + ", icons=" + Arrays.toString(icons) + "]";
	}
}
