package me.filoghost.chestcommands.inventory;

import me.filoghost.chestcommands.util.collection.Grid;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryGrid extends Grid<ItemStack> {

	private final Inventory inventory;

	public InventoryGrid(MenuInventoryHolder inventoryHolder, int rows, String title) {
		super(rows, 9);
		this.inventory = Bukkit.createInventory(inventoryHolder, getSize(), title);
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	protected ItemStack getByIndex0(int ordinalIndex) {
		return inventory.getItem(ordinalIndex);
	}

	@Override
	protected void setByIndex0(int ordinalIndex, ItemStack element) {
		inventory.setItem(ordinalIndex, element);
	}

}
