package com.gmail.filoghost.chestcommands.internal.icon;

import org.bukkit.inventory.ItemStack;

/**
 * An icon that will not change material, name, lore, ...
 */
public class StaticExtendedIcon extends ExtendedIcon {
	
	private ItemStack cachedItem;
	
	public StaticExtendedIcon() {
		super();
	}
	
	@Override
	public ItemStack createItemstack() {
		if (cachedItem == null) {
			cachedItem = super.createItemstack();
		}
		
		return cachedItem;
	}
	
}
