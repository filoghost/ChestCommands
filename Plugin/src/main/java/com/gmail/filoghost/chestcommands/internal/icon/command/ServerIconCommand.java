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
package com.gmail.filoghost.chestcommands.internal.icon.command;

import com.gmail.filoghost.chestcommands.bridge.bungee.BungeeCordUtils;
import com.gmail.filoghost.chestcommands.internal.icon.IconCommand;
import org.bukkit.entity.Player;

public class ServerIconCommand extends IconCommand {

	public ServerIconCommand(String command) {
		super(command);
	}

	@Override
	public void execute(Player player) {
		BungeeCordUtils.connect(player, hasVariables ? getParsedCommand(player) : command);
	}

}
