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
package me.filoghost.chestcommands.api.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.filoghost.chestcommands.api.IconMenu;
import me.filoghost.chestcommands.api.StaticIcon;
import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.api.ConfigurableIcon;
import me.filoghost.chestcommands.api.internal.BackendAPI;
import me.filoghost.chestcommands.menu.AdvancedIconMenu;

public class BackendAPIImpl extends BackendAPI {

	@Override
	public boolean isPluginMenu(String yamlFile) {
		return ChestCommands.getInstance().getMenuManager().getMenuByFileName(yamlFile) != null;
	}

	@Override
	public boolean openPluginMenu(Player player, String yamlFile) {
		AdvancedIconMenu menu = ChestCommands.getInstance().getMenuManager().getMenuByFileName(yamlFile);

		if (menu != null) {
			menu.open(player);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public ConfigurableIcon createConfigurableIcon(Material material) {
		ConfigurableIconImpl icon = new ConfigurableIconImpl();
		icon.setMaterial(material);
		return icon;
	}

	@Override
	public IconMenu createIconMenu(String title, int rows) {
		return new IconMenuImpl(title, rows);
	}

	@Override
	public StaticIcon createStaticIcon(ItemStack itemStack, boolean closeOnClick) {
		ItemStack itemStackCopy = itemStack.clone();
		
		return new StaticIcon() {

			@Override
			public ItemStack createItemStack(Player viewer) {
				return itemStackCopy;
			}

			@Override
			public boolean onClick(Player clicker) {
				return closeOnClick;
			}
			
		};
	}

}
