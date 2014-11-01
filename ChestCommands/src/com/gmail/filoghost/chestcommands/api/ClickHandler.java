package com.gmail.filoghost.chestcommands.api;

import org.bukkit.entity.Player;

public interface ClickHandler {

	/**
	 * 
	 * @param player - the player that clicked on the icon.
	 * @return true if the menu should be closed, false otherwise.
	 */
	public boolean onClick(Player player);
	
}
