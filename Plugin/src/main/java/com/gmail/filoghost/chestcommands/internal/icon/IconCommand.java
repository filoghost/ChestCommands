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
package com.gmail.filoghost.chestcommands.internal.icon;

import com.gmail.filoghost.chestcommands.config.AsciiPlaceholders;
import com.gmail.filoghost.chestcommands.internal.VariableManager;
import org.bukkit.entity.Player;

public abstract class IconCommand {

	protected String command;
	protected boolean hasVariables;

	public IconCommand(String command) {
		this.command = AsciiPlaceholders.placeholdersToSymbols(command).trim();
		this.hasVariables = VariableManager.hasVariables(command);
	}

	public String getParsedCommand(Player executor) {
		return hasVariables ? VariableManager.setVariables(command, executor) : command;
	}

	public abstract void execute(Player player);

}
