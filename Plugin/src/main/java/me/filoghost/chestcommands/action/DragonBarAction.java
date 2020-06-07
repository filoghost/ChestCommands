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

import me.filoghost.chestcommands.bridge.BarAPIBridge;
import me.filoghost.chestcommands.parser.FormatException;
import me.filoghost.chestcommands.parser.NumberParser;
import me.filoghost.chestcommands.util.FormatUtils;

public class DragonBarAction extends Action {

	private String message;
	private int seconds;

	public DragonBarAction(String action) {
		super(action);
		if (!hasVariables) {
			parseBar(super.action);
		}
	}

	private void parseBar(String action) {
		seconds = 1;
		message = action;

		String[] split = action.split("\\|", 2); // Max of 2 pieces
		if (split.length > 1) {
			try {
				seconds =  NumberParser.getStrictlyPositiveInteger(split[0].trim());
				message = split[1].trim();
			} catch (FormatException ex) {
				// Ignore
				// TODO: notify with message
			}
		}

		message = FormatUtils.addColors(message);
	}

	@Override
	public void execute(Player player) {
		if (hasVariables) {
			parseBar(getParsedAction(player));
		}
		if (BarAPIBridge.hasValidPlugin()) {
			BarAPIBridge.setMessage(player, message, seconds);
		}
	}

}
