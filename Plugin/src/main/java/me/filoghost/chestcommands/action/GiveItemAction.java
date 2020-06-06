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
package me.filoghost.chestcommands.action;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.filoghost.chestcommands.exception.FormatException;
import me.filoghost.chestcommands.util.ItemStackReader;

public class GiveItemAction extends Action {

	private ItemStack itemToGive;
	private String errorMessage;

	public GiveItemAction(String action) {
		super(action);
		if (!hasVariables) {
			parseItem(super.action);
		}
	}

	private void parseItem(String action) {
		try {
			ItemStackReader reader = new ItemStackReader(action, true);
			itemToGive = reader.createStack();
			errorMessage = null;
		} catch (FormatException e) {
			errorMessage = ChatColor.RED + "Invalid item to give: " + e.getMessage();
		}
	}

	@Override
	public void execute(Player player) {
		if (hasVariables) {
			parseItem(getParsedAction(player));
		}
		if (errorMessage != null) {
			player.sendMessage(errorMessage);
			return;
		}

		player.getInventory().addItem(itemToGive.clone());
	}

}
