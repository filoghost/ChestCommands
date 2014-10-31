package com.gmail.filoghost.chestcommands.nms;

import org.bukkit.inventory.ItemStack;

public class Fallback implements AttributeRemover {

	@Override
	public ItemStack removeAttributes(ItemStack item) {
		return item;
    }

}
