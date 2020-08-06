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
package me.filoghost.chestcommands.parsing.menu;

import com.google.common.collect.ImmutableList;
import me.filoghost.chestcommands.action.Action;
import me.filoghost.chestcommands.util.collection.CollectionUtils;

import java.util.List;

public class MenuSettings {

	// Required settings
	private final String title;
	private final int rows;

	// Optional settings
	private ImmutableList<String> commands;
	private ImmutableList<Action> openActions;
	private int refreshTicks;

	private MenuOpenItem openItem;
	
	public MenuSettings(String title, int rows) {
		this.title = title;
		this.rows = rows;
	}

	public String getTitle() {
		return title;
	}

	public int getRows() {
		return rows;
	}

	public void setCommands(List<String> commands) {
		this.commands = CollectionUtils.immutableCopy(commands);
	}

	public ImmutableList<String> getCommands() {
		return commands;
	}

	public ImmutableList<Action> getOpenActions() {
		return openActions;
	}

	public void setOpenActions(List<Action> openAction) {
		this.openActions = CollectionUtils.immutableCopy(openAction);
	}

	public int getRefreshTicks() {
		return refreshTicks;
	}

	public void setRefreshTicks(int refreshTicks) {
		this.refreshTicks = refreshTicks;
	}
	
	public MenuOpenItem getOpenItem() {
		return openItem;
	}

	public void setOpenItem(MenuOpenItem openItem) {
		this.openItem = openItem;
	}
	
}
