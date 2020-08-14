/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.api;

import me.filoghost.chestcommands.api.internal.BackendAPI;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;

import java.util.List;
import java.util.Map;

public interface ConfigurableIcon extends ClickableIcon {
	
	static ConfigurableIcon create(Material material) {
		return BackendAPI.getImplementation().createConfigurableIcon(material);
	}
	
	void setMaterial(Material material);

	Material getMaterial();

	void setAmount(int amount);

	int getAmount();

	void setDurability(short durability);

	short getDurability();

	void setNBTData(String nbtData);

	String getNBTData();

	void setName(String name);
	
	String getName();

	void setLore(String... lore);

	void setLore(List<String> lore);

	List<String> getLore();

	void setEnchantments(Map<Enchantment, Integer> enchantments);

	Map<Enchantment, Integer> getEnchantments();

	void addEnchantment(Enchantment enchantment);

	void addEnchantment(Enchantment enchantment, Integer level);

	void removeEnchantment(Enchantment enchantment);

	Color getLeatherColor();

	void setLeatherColor(Color leatherColor);

	String getSkullOwner();

	void setSkullOwner(String skullOwner);

	DyeColor getBannerColor();

	void setBannerColor(DyeColor bannerColor);

	List<Pattern> getBannerPatterns();

	void setBannerPatterns(List<Pattern> bannerPatterns);

	void setPlaceholdersEnabled(boolean enabled);

}
