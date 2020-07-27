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
package me.filoghost.chestcommands;

import me.filoghost.chestcommands.api.ConfigurableIcon;
import me.filoghost.chestcommands.api.IconMenu;
import me.filoghost.chestcommands.api.PlaceholderReplacer;
import me.filoghost.chestcommands.api.StaticIcon;
import me.filoghost.chestcommands.api.internal.BackendAPI;
import me.filoghost.chestcommands.icon.APIConfigurableIcon;
import me.filoghost.chestcommands.icon.APIStaticIcon;
import me.filoghost.chestcommands.menu.APIIconMenu;
import me.filoghost.chestcommands.menu.InternalIconMenu;
import me.filoghost.chestcommands.placeholder.PlaceholderManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class DefaultBackendAPI extends BackendAPI {

	@Override
	public boolean isPluginMenu(String yamlFile) {
		return ChestCommands.getMenuManager().getMenuByFileName(yamlFile) != null;
	}

	@Override
	public boolean openPluginMenu(Player player, String yamlFile) {
		InternalIconMenu menu = ChestCommands.getMenuManager().getMenuByFileName(yamlFile);

		if (menu != null) {
			menu.open(player);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public ConfigurableIcon createConfigurableIcon(Material material) {
		return new APIConfigurableIcon(material);
	}

	@Override
	public IconMenu createIconMenu(Plugin owner, String title, int rows) {
		return new APIIconMenu(owner, title, rows);
	}

	@Override
	public StaticIcon createStaticIcon(ItemStack itemStack) {
		return new APIStaticIcon(itemStack);
	}

	@Override
	public void registerPlaceholder(Plugin plugin, String identifier, PlaceholderReplacer placeholderReplacer) {
		PlaceholderManager.registerPluginPlaceholder(plugin, identifier, placeholderReplacer);
	}

}
