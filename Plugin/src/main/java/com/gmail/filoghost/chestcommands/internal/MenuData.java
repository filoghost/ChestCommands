package com.gmail.filoghost.chestcommands.internal;

import java.util.List;

import org.bukkit.Material;

import com.gmail.filoghost.chestcommands.internal.icon.IconCommand;
import com.gmail.filoghost.chestcommands.util.ClickType;

public class MenuData {

	// Required data.
	private String title;
	private int rows;
	
	// Optional data.
	private String[] commands;
	private Material boundMaterial;
	private short boundDataValue;
	private ClickType clickType;
	private List<IconCommand> openActions;
	private int refreshTenths;
	
	public MenuData(String title, int rows) {
		this.title = title;
		this.rows = rows;
		boundDataValue = -1; // -1 = any.
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

	public List<IconCommand> getOpenActions() {
		return openActions;
	}

	public void setOpenActions(List<IconCommand> openAction) {
		this.openActions = openAction;
	}

	public int getRefreshTenths() {
		return refreshTenths;
	}

	public void setRefreshTenths(int refreshTenths) {
		this.refreshTenths = refreshTenths;
	}
}
