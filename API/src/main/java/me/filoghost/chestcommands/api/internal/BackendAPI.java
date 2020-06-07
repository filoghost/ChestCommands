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
package me.filoghost.chestcommands.api.internal;

import org.bukkit.Material;

import me.filoghost.chestcommands.api.Icon;
import me.filoghost.chestcommands.api.IconMenu;

public abstract class BackendAPI {
	
	private static BackendAPI implementation;
	
	public static void setImplementation(BackendAPI implementation) {
		BackendAPI.implementation = implementation;
	}
	
	public static BackendAPI getImplementation() {
		if (implementation == null) {
			throw new IllegalStateException("no implementation set");
		}
		
		return implementation;
	}

	public abstract IconMenu getMenuByFileName(String yamlFile);

	public abstract Icon createIcon(Material material);

	public abstract IconMenu createIconMenu(String title, int rows);

}
