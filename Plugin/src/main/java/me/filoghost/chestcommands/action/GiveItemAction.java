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
package me.filoghost.chestcommands.action;

import me.filoghost.chestcommands.parsing.ItemStackParser;
import me.filoghost.chestcommands.parsing.ParseException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveItemAction implements Action {

	private final ItemStack itemToGive;

	public GiveItemAction(String serializedAction) throws ParseException {
		ItemStackParser reader = new ItemStackParser(serializedAction, true);
		itemToGive = reader.createStack();
	}

	@Override
	public void execute(Player player) {
		player.getInventory().addItem(itemToGive.clone());
	}

}
