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

public abstract class Action {

	private String errorMessage;

	protected void disable(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public final void execute(Player player) {
		if (errorMessage != null) {
			player.sendMessage(errorMessage);
		} else {
			execute0(player);
		}
	}
	
	protected abstract void execute0(Player player);

}
