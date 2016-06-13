package com.gmail.filoghost.chestcommands.internal;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.chestcommands.util.ClickType;
import com.gmail.filoghost.chestcommands.util.Validate;

public class BoundItem {

	private ExtendedIconMenu menu;
	private Material material;
	private short data;
	private ClickType clickType;
	
	public BoundItem(ExtendedIconMenu menu, Material material, ClickType clickType) {
		Validate.notNull(material, "Material cannot be null");
		Validate.notNull(material, "ClickType cannot be null");
		Validate.isTrue(material != Material.AIR, "Material cannot be AIR");
		
		this.menu = menu;
		this.material = material;
		this.clickType = clickType;
		data = -1; // -1 = any.
	}
	
	public void setRestrictiveData(short data) {
		this.data = data;
	}
	
	public ExtendedIconMenu getMenu() {
		return menu;
	}
	
	public boolean isValidTrigger(ItemStack item, Action action) {
		if (item == null) {
			return false;
		}
		
		// First, they must have the same material.
		if (this.material != item.getType()) {
			return false;
		}
		// Check if the data value is valid (-1 = any data value).
		if (this.data != -1 && this.data != item.getDurability()) {
			return false;
		}
		
		// Finally checks the action.
		return clickType.isValidInteract(action);
	}
}
