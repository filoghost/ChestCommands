package me.filoghost.chestcommands.icon.requirement.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class RemainingItem {

	private final int slotIndex;
	private final Material material;
	private final short durability;
	private int amount;

	public RemainingItem(int slotIndex, ItemStack item) {
		this.slotIndex = slotIndex;
		this.material = item.getType();
		this.durability = item.getDurability();
		this.amount = item.getAmount();
	}

	public int getSlotIndex() {
		return slotIndex;
	}

	public Material getMaterial() {
		return material;
	}

	public short getDurability() {
		return durability;
	}

	public int getAmount() {
		return amount;
	}

	public int subtract(int minusAmount) {
		int subtractedAmount = Math.min(minusAmount, this.amount);

		this.amount -= subtractedAmount;
		return subtractedAmount;
	}

}
