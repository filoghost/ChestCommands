package com.gmail.filoghost.chestcommands.internal;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.chestcommands.util.Validate;

public class RequiredItem {

	private Material material;
	private int amount;
	private short dataValue;
	private boolean isDurabilityRestrictive = false;
	
	public RequiredItem(Material material, int amount) {
		Validate.notNull(material, "Material cannot be null");
		Validate.isTrue(material != Material.AIR, "Material cannot be air");
		
		this.material = material;
		this.amount = amount;
	}
	
	public ItemStack createItemStack() {
		return new ItemStack(material, amount, dataValue);
	}
	
	public Material getMaterial() {
		return material;
	}

	public int getAmount() {
		return amount;
	}

	public short getDataValue() {
		return dataValue;
	}

	public void setRestrictiveDataValue(short data) {
		Validate.isTrue(data >= 0, "Data value cannot be negative");

		this.dataValue = data;
		isDurabilityRestrictive = true;
	}
	
	public boolean hasRestrictiveDataValue() {
		return isDurabilityRestrictive;
	}
	
	public boolean isValidDataValue(short data) {
		if (!isDurabilityRestrictive) return true;
		return data == this.dataValue;
	}
	
	public boolean hasItem(Player player) {
		int amountFound = 0;
		
		for (ItemStack item : player.getInventory().getContents()) {
			if (item != null && item.getType() == material && isValidDataValue(item.getDurability())) {
				amountFound += item.getAmount();
			}
		}
		
		return amountFound >= amount;
	}
	
	public boolean takeItem(Player player) {
		if (amount <= 0) {
			return true;
		}
		
		int itemsToTake = amount; //start from amount and decrease
		
		ItemStack[] contents = player.getInventory().getContents();
		ItemStack current = null;
		
		
		for (int i = 0; i < contents.length; i++) {

			current = contents[i];
			
			if (current != null && current.getType() == material && isValidDataValue(current.getDurability())) {
				if (current.getAmount() > itemsToTake) {
					current.setAmount(current.getAmount() - itemsToTake);
					return true;
				} else {
					itemsToTake -= current.getAmount();
					player.getInventory().setItem(i, new ItemStack(Material.AIR));
				}
			}
			
			// The end
			if (itemsToTake <= 0) return true;
		}
		
		return false;
	}
}
