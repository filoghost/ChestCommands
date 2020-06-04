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
package me.filoghost.chestcommands.task;

import org.bukkit.entity.Player;

import me.filoghost.chestcommands.api.Icon;

public class ExecuteCommandsTask implements Runnable {

	private Player player;
	private Icon icon;


	public ExecuteCommandsTask(Player player, Icon icon) {
		this.player = player;
		this.icon = icon;
	}


	@Override
	public void run() {
		boolean close = icon.onClick(player);

		if (close) {
			player.closeInventory();
		}
	}


}
