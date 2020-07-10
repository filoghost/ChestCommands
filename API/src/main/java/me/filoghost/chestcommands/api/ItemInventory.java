package me.filoghost.chestcommands.api;

import org.bukkit.entity.Player;

public interface ItemInventory {

	void refresh();

	Player getViewer();

	IconMenu getIconMenu();

}
