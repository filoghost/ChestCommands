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

import org.bukkit.entity.Player;

import me.filoghost.chestcommands.util.Colors;
import me.filoghost.chestcommands.placeholder.RelativeString;

public class SendMessageAction extends Action {
	
	private final RelativeString message;

	public SendMessageAction(String action) {
		message = RelativeString.of(Colors.addColors(action));
	}

	@Override
	protected void execute0(Player player) {
		player.sendMessage(message.getValue(player));
	}

}
