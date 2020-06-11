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

import me.filoghost.chestcommands.action.Action;
import me.filoghost.chestcommands.action.OpenMenuAction;
import me.filoghost.chestcommands.api.ClickHandler;
import me.filoghost.chestcommands.api.ClickResult;

import java.util.List;

public class RunActionsClickHandler implements ClickHandler {

	private List<Action> actions;
	private boolean forceClose;

	public RunActionsClickHandler(List<Action> actions) {
		this.actions = actions;

		if (actions != null && actions.size() > 0) {
			for (Action action : actions) {
				if (action instanceof OpenMenuAction) {
					// Fix GUI closing if KEEP-OPEN is not set, and a command should open another GUI
					this.forceClose = true;
				}
			}
		}
	}

	@Override
	public ClickResult onClick(Player player) {
		if (actions != null && actions.size() > 0) {
			for (Action action : actions) {
				action.execute(player);
			}
		}
		
		if (forceClose) {
			return ClickResult.CLOSE;
		}

		return ClickResult.DEFAULT;
	}

}
