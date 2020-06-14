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
package me.filoghost.chestcommands.menu.icon;

import org.bukkit.entity.Player;

import com.google.common.base.Preconditions;

import me.filoghost.chestcommands.ChestCommands;

public class RequiredExpLevel implements Requirement {

	private final int levels;
	
	public RequiredExpLevel(int levels) {
		Preconditions.checkArgument(levels > 0, "levels must be positive");
		this.levels = levels;
	}
	
	public int getLevels() {
		return levels;
	}

	@Override
	public boolean check(Player player) {
		if (player.getLevel() < levels) {
			player.sendMessage(ChestCommands.getLang().no_exp.replace("{levels}", Integer.toString(levels)));
			return false;
		}
		
		return true;
	}

	@Override
	public boolean takeCost(Player player) {
		int newLevel = player.getLevel() - levels;
		if (newLevel < 0) {
			newLevel = 0;
		}
		
		player.setLevel(newLevel);
		return true;
	}	

}
