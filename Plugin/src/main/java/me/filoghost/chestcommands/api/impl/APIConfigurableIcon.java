package me.filoghost.chestcommands.api.impl;

import me.filoghost.chestcommands.api.ClickHandler;
import me.filoghost.chestcommands.api.ConfigurableIcon;
import me.filoghost.chestcommands.icon.BaseConfigurableIcon;
import org.bukkit.Material;

public class APIConfigurableIcon extends BaseConfigurableIcon implements ConfigurableIcon {

	private ClickHandler clickHandler;

	public APIConfigurableIcon(Material material) {
		super(material);
	}

	@Override
	public void setClickHandler(ClickHandler clickHandler) {
		this.clickHandler = clickHandler;
	}

	@Override
	public ClickHandler getClickHandler() {
		return clickHandler;
	}

}
