package me.filoghost.chestcommands.icon.requirement.item;

import me.filoghost.chestcommands.util.MaterialsHelper;
import me.filoghost.chestcommands.util.Preconditions;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryTakeHelper {

	private final PlayerInventory inventory;
	private final List<RemainingItem> remainingItems;

	private boolean success;

	public InventoryTakeHelper(PlayerInventory inventory) {
		this.inventory = inventory;
		this.remainingItems = new ArrayList<>();

		for (int slotIndex = 0; slotIndex < inventory.getSize(); slotIndex++) {
			ItemStack item = inventory.getItem(slotIndex);
			if (item != null && !MaterialsHelper.isAir(item.getType())) {
				remainingItems.add(new RemainingItem(slotIndex, item));
			}
		}
	}

	public List<RequiredItem> prepareTakeItems(List<RequiredItem> requiredItems) {
		List<RequiredItem> missingItems = new ArrayList<>();

		// Sort required items: check required items with a restrictive durability first
		List<RequiredItem> sortedRequiredItems = requiredItems.stream()
				.sorted(Comparator.comparing(RequiredItem::hasRestrictiveDurability).reversed())
				.collect(Collectors.toList());

		for (RequiredItem requiredItem : sortedRequiredItems) {
			int remainingRequiredAmount = requiredItem.getAmount();

			for (RemainingItem remainingItem : remainingItems) {
				if (remainingItem.getAmount() > 0 && requiredItem.isMatchingType(remainingItem)) {
					int takenAmount = remainingItem.subtract(remainingRequiredAmount);
					remainingRequiredAmount -= takenAmount;
					if (remainingRequiredAmount == 0) {
						break;
					}
				}
			}

			// Couldn't take the required amount of an item
			if (remainingRequiredAmount > 0) {
				missingItems.add(requiredItem);
			}
		}

		success = missingItems.isEmpty();
		return missingItems;
	}

	public void applyTakeItems() {
		Preconditions.checkState(success, "items take preparation was not run or successful");

		for (RemainingItem remainingItem : remainingItems) {
			int slotIndex = remainingItem.getSlotIndex();
			ItemStack inventoryItem = inventory.getItem(slotIndex);
			if (remainingItem.getAmount() != inventoryItem.getAmount()) {
				if (remainingItem.getAmount() > 0) {
					inventoryItem.setAmount(remainingItem.getAmount());
				} else {
					inventory.setItem(slotIndex, null);
				}
			}
		}
	}

}
