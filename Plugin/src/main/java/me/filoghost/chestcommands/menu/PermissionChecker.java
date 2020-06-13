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
package me.filoghost.chestcommands.menu;

import org.bukkit.entity.Player;

import me.filoghost.chestcommands.util.StringUtils;

public class PermissionChecker {
	
	private final String permission;
	private final boolean negated;
	
	public PermissionChecker(String permission) {
		if (permission != null) {
			permission = permission.trim();
		}
		
		if (StringUtils.isNullOrEmpty(permission)) {
			this.permission = null;
			negated = false;
		} else {
			if (permission.startsWith("-")) {
				this.permission = permission.substring(1, permission.length());
				negated = true;
			} else {
				this.permission = permission;
				negated = false;
			}
		}
	}
	
	public boolean hasPermission(Player player) {
		if (isEmpty()) {
			return true;
		}
		
		if (negated) {
			return !player.hasPermission(permission);
		} else {
			return player.hasPermission(permission);
		}
	}

	public boolean isEmpty() {
		return this.permission == null;
	}
	
	

}
