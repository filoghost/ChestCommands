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

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.filoghost.chestcommands.parsing.ParseException;
import me.filoghost.chestcommands.hook.BarAPIHook;
import me.filoghost.chestcommands.parsing.NumberParser;
import me.filoghost.chestcommands.util.Colors;
import me.filoghost.chestcommands.util.Strings;
import me.filoghost.chestcommands.variable.RelativeString;

public class DragonBarAction extends Action {

	private RelativeString message;
	private int seconds;

	public DragonBarAction(String serialiazedAction) {
		seconds = 1;
		String message = serialiazedAction;
		
		String[] split = Strings.trimmedSplit(serialiazedAction, "\\|", 2); // Max of 2 pieces
		if (split.length > 1) {
			try {
				seconds =  NumberParser.getStrictlyPositiveInteger(split[0]);
				message = split[1];
			} catch (ParseException ex) {
				disable(ChatColor.RED + "Invalid dragon bar time: " + split[0]);
				return;
			}
		}

		this.message = RelativeString.of(Colors.addColors(message));
	}

	@Override
	protected void execute0(Player player) {
		if (BarAPIHook.INSTANCE.isEnabled()) {
			BarAPIHook.setMessage(player, message.getValue(player), seconds);
		}
	}

}
