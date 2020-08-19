/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.icon;

import me.filoghost.chestcommands.api.ClickHandler;
import me.filoghost.chestcommands.api.StaticIcon;
import me.filoghost.fcommons.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class APIStaticIcon implements StaticIcon {

	private ItemStack itemStack;
	private ClickHandler clickHandler;

	public APIStaticIcon(ItemStack itemStack) {
		Preconditions.notNull(itemStack, "itemStack");
		this.itemStack = itemStack;
	}

	@Override
	public ItemStack getItemStack() {
		return itemStack;
	}

	@Override
	public void setItemStack(ItemStack itemStack) {
		Preconditions.notNull(itemStack, "itemStack");
		this.itemStack = itemStack;
	}

	@Override
	public ClickHandler getClickHandler() {
		return clickHandler;
	}

	@Override
	public void setClickHandler(ClickHandler clickHandler) {
		this.clickHandler = clickHandler;
	}

	@Override
	public ItemStack render(Player viewer) {
		return itemStack;
	}

}
