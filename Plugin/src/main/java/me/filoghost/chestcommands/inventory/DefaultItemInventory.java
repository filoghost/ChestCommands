package me.filoghost.chestcommands.inventory;

import me.filoghost.chestcommands.api.ClickResult;
import me.filoghost.chestcommands.api.Icon;
import me.filoghost.chestcommands.api.ItemInventory;
import me.filoghost.chestcommands.icon.RefreshableIcon;
import me.filoghost.chestcommands.menu.BaseIconMenu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a particular view of a menu.
 */
public class DefaultItemInventory implements ItemInventory {

	private final BaseIconMenu menu;
	private final InventoryGrid bukkitInventory;
	private final Player viewer;

	public DefaultItemInventory(BaseIconMenu menu, Player viewer) {
		this.menu = menu;
		this.viewer = viewer;
		this.bukkitInventory = new InventoryGrid(new ItemInventoryHolder(this), menu.getRowCount(), menu.getTitle());
		refresh();
	}

	@Override
	public void refresh() {
		for (int i = 0; i < menu.getIcons().getSize(); i++) {
			Icon icon = menu.getIcons().getByIndex(i);

			if (icon == null) {
				bukkitInventory.setByIndex(i, null);
			} else if (icon instanceof RefreshableIcon) {
				ItemStack newItemStack = ((RefreshableIcon) icon).updateRendering(viewer, bukkitInventory.getByIndex(i));
				bukkitInventory.setByIndex(i, newItemStack);
			} else {
				bukkitInventory.setByIndex(i, icon.render(viewer));
			}
		}
	}

	public void open(Player viewer) {
		viewer.openInventory(bukkitInventory.getInventory());
	}

	public SlotClickHandler getSlotClickHandler(int slot, Player clicker) {
		if (slot < 0 || slot >= bukkitInventory.getSize()) {
			return null;
		}

		Icon icon = menu.getIcons().getByIndex(slot);
		if (icon == null) {
			return null;
		}

		return () -> icon.onClick(this, clicker);
	}

	@Override
	public BaseIconMenu getIconMenu() {
		return menu;
	}

	@Override
	public Player getViewer() {
		return viewer;
	}


	@FunctionalInterface
	public interface SlotClickHandler {

		ClickResult onClick();

	}

}
