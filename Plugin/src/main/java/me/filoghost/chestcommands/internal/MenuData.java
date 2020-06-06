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

import org.bukkit.Material;

import me.filoghost.chestcommands.action.Action;
import me.filoghost.chestcommands.util.ClickType;

import java.util.List;

public class MenuData {

	// Required data
	private String title;
	private int rows;

	// Optional data
	private String[] commands;
	private Material boundMaterial;
	private short boundDataValue;
	private ClickType clickType;
	private List<Action> openActions;
	private int refreshTenths;

	public MenuData(String title, int rows) {
		this.title = title;
		this.rows = rows;
		boundDataValue = -1; // -1 = any
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

	public boolean hasBoundMaterial() {
		return boundMaterial != null;
	}

	public Material getBoundMaterial() {
		return boundMaterial;
	}

	public void setBoundMaterial(Material boundMaterial) {
		this.boundMaterial = boundMaterial;
	}

	public boolean hasBoundDataValue() {
		return boundDataValue > -1;
	}

	public short getBoundDataValue() {
		return boundDataValue;
	}

	public void setBoundDataValue(short boundDataValue) {
		this.boundDataValue = boundDataValue;
	}

	public ClickType getClickType() {
		return clickType;
	}

	public void setClickType(ClickType clickType) {
		this.clickType = clickType;
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
}
