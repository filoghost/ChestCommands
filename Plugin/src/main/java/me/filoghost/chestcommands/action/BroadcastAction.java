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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.filoghost.chestcommands.util.FormatUtils;
import me.filoghost.chestcommands.variable.RelativeString;

public class BroadcastAction extends Action {
	
	private final RelativeString message;

	public BroadcastAction(String action) {
		message = RelativeString.of(FormatUtils.addColors(action));
	}

	@Override
	protected void executeInner(Player player) {
		Bukkit.broadcastMessage(message.getValue(player));
	}

}
