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

import me.filoghost.chestcommands.action.Action;

import java.util.List;

public class MenuSettings {

	// Required settings
	private final String title;
	private final int rows;

	// Optional settings
	private String[] commands;
	private List<Action> openActions;
	private int refreshTenths;

	private OpenTrigger openTrigger;
	
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

	public boolean hasCommands() {
		return commands != null && commands.length > 0;
	}

	public void setCommands(String[] commands) {
		this.commands = commands;
	}

	public String[] getCommands() {
		return commands;
	}

	public List<Action> getOpenActions() {
		return openActions;
	}

	public void setOpenActions(List<Action> openAction) {
		this.openActions = openAction;
	}

	public int getRefreshTenths() {
		return refreshTenths;
	}

	public void setRefreshTenths(int refreshTenths) {
		this.refreshTenths = refreshTenths;
	}
	
	public OpenTrigger getOpenTrigger() {
		return openTrigger;
	}

	public void setOpenTrigger(OpenTrigger openTrigger) {
		this.openTrigger = openTrigger;
	}
	
}
