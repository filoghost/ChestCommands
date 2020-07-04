package me.filoghost.chestcommands.api;

import org.bukkit.entity.Player;

public interface ClickableIcon extends Icon {

	void setClickHandler(ClickHandler clickHandler);

	ClickHandler getClickHandler();

	@Override
	default ClickResult onClick(ItemInventory itemInventory, Player clicker) {
		if (getClickHandler() != null) {
			return getClickHandler().onClick(clicker);
		} else {
			return ClickResult.KEEP_OPEN;
		}
	}

}
