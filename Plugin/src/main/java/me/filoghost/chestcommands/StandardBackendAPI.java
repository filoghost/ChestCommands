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
package me.filoghost.chestcommands;

import org.bukkit.Material;

import me.filoghost.chestcommands.api.Icon;
import me.filoghost.chestcommands.api.IconMenu;
import me.filoghost.chestcommands.api.internal.BackendAPI;
import me.filoghost.chestcommands.internal.BasicIcon;
import me.filoghost.chestcommands.internal.BasicIconMenu;

public class StandardBackendAPI extends BackendAPI {

	@Override
	public IconMenu getMenuByFileName(String yamlFile) {
		return ChestCommands.getInstance().getMenuManager().getMenuByFileName(yamlFile);
	}

	@Override
	public Icon createIcon(Material material) {
		BasicIcon icon = new BasicIcon();
		icon.setMaterial(material);
		return icon;
	}

	@Override
	public IconMenu createIconMenu(String title, int rows) {
		return new BasicIconMenu(title, rows);
	}

}
