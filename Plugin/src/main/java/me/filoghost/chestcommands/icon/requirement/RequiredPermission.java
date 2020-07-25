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
package me.filoghost.chestcommands.icon.requirement;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.util.Strings;
import org.bukkit.entity.Player;

public class RequiredPermission implements Requirement {
	
	private final String permission;
	private final boolean negated;
	private String noPermissionMessage;
	
	public RequiredPermission(String permission) {
		if (permission != null) {
			permission = permission.trim();
		}
		
		if (Strings.isNullOrEmpty(permission)) {
			this.permission = null;
			negated = false;
		} else {
			if (permission.startsWith("-")) {
				this.permission = permission.substring(1);
				negated = true;
			} else {
				this.permission = permission;
				negated = false;
			}
		}
	}
	
	
	public void setNoPermissionMessage(String noPermissionMessage) {
		this.noPermissionMessage = noPermissionMessage;
	}


	@Override
	public boolean hasCost(Player player) {
		if (hasPermission(player)) {
			return true;
		} else {
			if (noPermissionMessage != null) {
				player.sendMessage(noPermissionMessage);
			} else {
				player.sendMessage(ChestCommands.getLang().default_no_icon_permission);
			}
			return false;
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
	
	@Override
	public boolean takeCost(Player player) {
		return true;
	}

	public boolean isEmpty() {
		return this.permission == null;
	}	

}
