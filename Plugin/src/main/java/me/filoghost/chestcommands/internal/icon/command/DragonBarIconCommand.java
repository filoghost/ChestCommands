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
package me.filoghost.chestcommands.internal.icon.command;

import org.bukkit.entity.Player;

import me.filoghost.chestcommands.bridge.BarAPIBridge;
import me.filoghost.chestcommands.internal.icon.IconCommand;
import me.filoghost.chestcommands.util.FormatUtils;
import me.filoghost.chestcommands.util.Utils;

public class DragonBarIconCommand extends IconCommand {

	private String message;
	private int seconds;

	public DragonBarIconCommand(String command) {
		super(command);
		if (!hasVariables) {
			parseBar(super.command);
		}
	}

	private void parseBar(String command) {
		seconds = 1;
		message = command;

		String[] split = command.split("\\|", 2); // Max of 2 pieces
		if (split.length > 1 && Utils.isValidPositiveInteger(split[0].trim())) {
			seconds = Integer.parseInt(split[0].trim());
			message = split[1].trim();
		}

		message = FormatUtils.addColors(message);
	}

	@Override
	public void execute(Player player) {
		if (hasVariables) {
			parseBar(getParsedCommand(player));
		}
		if (BarAPIBridge.hasValidPlugin()) {
			BarAPIBridge.setMessage(player, message, seconds);
		}
	}

}
