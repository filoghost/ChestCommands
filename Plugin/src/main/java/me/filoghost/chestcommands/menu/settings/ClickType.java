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
package me.filoghost.chestcommands.menu.settings;

import org.bukkit.event.block.Action;

public enum ClickType {

	LEFT,
	RIGHT,
	BOTH;

	public static ClickType fromOptions(boolean left, boolean right) {
		if (left && right) {
			return BOTH;
		} else if (left) {
			return LEFT;
		} else if (right) {
			return RIGHT;
		} else {
			return null;
		}
	}

	public boolean isValidInteract(Action action) {
		if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
			return this == LEFT || this == BOTH;
		} else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
			return this == RIGHT || this == BOTH;
		} else {
			return false;
		}
	}

}
