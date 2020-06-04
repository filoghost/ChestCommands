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
package me.filoghost.chestcommands.internal;

import org.bukkit.entity.Player;

import me.filoghost.chestcommands.api.ClickHandler;
import me.filoghost.chestcommands.internal.icon.IconCommand;
import me.filoghost.chestcommands.internal.icon.command.OpenIconCommand;

import java.util.List;

public class CommandsClickHandler implements ClickHandler {

	private List<IconCommand> commands;
	private boolean closeOnClick;

	public CommandsClickHandler(List<IconCommand> commands, boolean closeOnClick) {
		this.commands = commands;
		this.closeOnClick = closeOnClick;

		if (commands != null && commands.size() > 0) {
			for (IconCommand command : commands) {
				if (command instanceof OpenIconCommand) {
					// Fix GUI closing if KEEP-OPEN is not set, and a command should open another GUI
					this.closeOnClick = false;
				}
			}
		}
	}

	@Override
	public boolean onClick(Player player) {
		if (commands != null && commands.size() > 0) {
			for (IconCommand command : commands) {
				command.execute(player);
			}
		}

		return closeOnClick;
	}

}
