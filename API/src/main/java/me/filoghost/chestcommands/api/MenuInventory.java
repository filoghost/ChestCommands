package me.filoghost.chestcommands.api;

import org.bukkit.entity.Player;

public interface MenuInventory {

	void refresh();

	Player getViewer();

	IconMenu getIconMenu();

}
