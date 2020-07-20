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
package me.filoghost.chestcommands.icon;

import me.filoghost.chestcommands.api.ClickHandler;
import me.filoghost.chestcommands.api.StaticIcon;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class APIStaticIcon implements StaticIcon {

	private ItemStack itemStack;
	private ClickHandler clickHandler;

	public APIStaticIcon(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	@Override
	public ItemStack getItemStack() {
		return itemStack;
	}

	@Override
	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	@Override
	public ClickHandler getClickHandler() {
		return clickHandler;
	}

	@Override
	public void setClickHandler(ClickHandler clickHandler) {
		this.clickHandler = clickHandler;
	}

	@Override
	public ItemStack render(Player viewer) {
		return itemStack;
	}

}
