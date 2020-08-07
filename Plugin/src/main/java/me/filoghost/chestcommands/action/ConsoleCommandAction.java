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
package me.filoghost.chestcommands.action;

import me.filoghost.chestcommands.placeholder.PlaceholderString;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ConsoleCommandAction implements Action {

	private final PlaceholderString command;

	public ConsoleCommandAction(String serializedAction) {
		command = PlaceholderString.of(serializedAction);
	}

	@Override
	public void execute(Player player) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.getValue(player));
	}

}
