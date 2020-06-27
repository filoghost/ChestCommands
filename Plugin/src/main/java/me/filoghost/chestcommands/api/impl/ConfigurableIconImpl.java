/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.chestcommands.api.impl;

import me.filoghost.chestcommands.api.ClickHandler;
import me.filoghost.chestcommands.api.ClickResult;
import me.filoghost.chestcommands.api.ConfigurableIcon;
import me.filoghost.chestcommands.util.Log;
import me.filoghost.chestcommands.util.Preconditions;
import me.filoghost.chestcommands.util.Utils;
import me.filoghost.chestcommands.variable.RelativeString;
import me.filoghost.chestcommands.variable.RelativeStringList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ConfigurableIconImpl implements ConfigurableIcon {

	private Material material;
	private int amount;
	private short durability;

	private String nbtData;
	private RelativeString name;
	private RelativeStringList lore;
	private Map<Enchantment, Integer> enchantments;
	private Color leatherColor;
	private RelativeString skullOwner;
	private DyeColor bannerColor;
	private List<Pattern> bannerPatterns;

	protected boolean closeOnClick;
	private ClickHandler clickHandler;
	
	private ItemStack cachedItem; // When there are no variables, we don't recreate the item

	public ConfigurableIconImpl(Material material) {
		Preconditions.checkArgumentNotAir(material, "material");
		this.material = material;
		this.amount = 1;
		this.closeOnClick = true;
	}

	public boolean hasVariables() {
		return (name != null && name.hasVariables())
				|| (lore != null && lore.hasVariables())
				|| (skullOwner != null && skullOwner.hasVariables());
	}

	@Override
	public void setMaterial(Material material) {
		Preconditions.checkArgumentNotAir(material, "material");
		this.material = material;
	}

	@Override
	public Material getMaterial() {
		return material;
	}

	@Override
	public void setAmount(int amount) {
		Preconditions.checkArgument(amount >= 1, "Amount must 1 or greater");
		this.amount = Math.min(amount, 127);
	}

	@Override
	public int getAmount() {
		return amount;
	}

	@Override
	public void setDurability(short durability) {
		Preconditions.checkArgument(durability >= 0, "Durability must not be negative");
		this.durability = durability;
	}

	@Override
	public short getDurability() {
		return durability;
	}

	@Override
	public void setNBTData(String nbtData) {
		this.nbtData = nbtData;
	}

	@Override
	public String getNBTData() {
		return nbtData;
	}

	@Override
	public void setName(String name) {
		this.name = RelativeString.of(name);
	}

	@Override
	public boolean hasName() {
		return name != null;
	}
	
	public String getName() {
		if (name != null) {
			return name.getRawValue();
		} else {
			return null;
		}
	}

	@Override
	public void setLore(String... lore) {
		if (lore != null) {
			setLore(Arrays.asList(lore));
		}
	}

	@Override
	public void setLore(List<String> lore) {
		if (!Utils.isNullOrEmpty(lore)) {
			this.lore = new RelativeStringList(lore);
		} else {
			this.lore = null;
		}
	}

	@Override
	public boolean hasLore() {
		return lore != null;
	}

	@Override
	public List<String> getLore() {
		if (lore != null) {
			return new ArrayList<>(lore.getRawValue());
		} else {
			return null;
		}
	}

	@Override
	public void setEnchantments(Map<Enchantment, Integer> enchantments) {
		this.enchantments = Utils.nullableCopy(enchantments);
	}

	@Override
	public Map<Enchantment, Integer> getEnchantments() {
		return Utils.nullableCopy(enchantments);
	}

	@Override
	public void addEnchantment(Enchantment ench) {
		addEnchantment(ench, 1);
	}

	@Override
	public void addEnchantment(Enchantment ench, Integer level) {
		if (enchantments == null) {
			enchantments = new HashMap<>();
		}
		enchantments.put(ench, level);
	}

	@Override
	public void removeEnchantment(Enchantment ench) {
		if (enchantments == null) {
			return;
		}
		enchantments.remove(ench);
	}

	@Override
	public Color getLeatherColor() {
		return leatherColor;
	}

	@Override
	public void setLeatherColor(Color leatherColor) {
		this.leatherColor = leatherColor;
	}

	@Override
	public String getSkullOwner() {
		if (skullOwner != null) {
			return skullOwner.getRawValue();
		} else {
			return null;
		}
	}

	@Override
	public void setSkullOwner(String skullOwner) {
		this.skullOwner = RelativeString.of(skullOwner);
	}

	@Override
	public DyeColor getBannerColor() {
		return bannerColor;
	}

	@Override
	public void setBannerColor(DyeColor bannerColor) {
		this.bannerColor = bannerColor;
	}

	@Override
	public List<Pattern> getBannerPatterns() {
		return Utils.nullableCopy(bannerPatterns);
	}

	@Override
	public void setBannerPatterns(List<Pattern> bannerPatterns) {
		this.bannerPatterns = Utils.nullableCopy(bannerPatterns);
	}

	@Override
	public void setCloseOnClick(boolean closeOnClick) {
		this.closeOnClick = closeOnClick;
	}

	@Override
	public void setClickHandler(ClickHandler clickHandler) {
		this.clickHandler = clickHandler;
	}

	@Override
	public ClickHandler getClickHandler() {
		return clickHandler;
	}

	public String calculateName(Player viewer) {
		if (!hasName()) {
			return null;
		}

		String name = this.name.getValue(viewer);

		if (name.isEmpty()) {
			// Add a color to display the name empty
			return ChatColor.WHITE.toString();
		} else {
			return name;
		}
	}

	public List<String> calculateLore(Player viewer) {
		if (hasLore()) {
			return lore.getValue(viewer);
		} else {
			return null;
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public ItemStack createItemStack(Player viewer) {

		if (!this.hasVariables() && cachedItem != null) {
			// Performance: return a static item
			return cachedItem;
		}

		ItemStack itemStack = new ItemStack(material, amount, durability);

		// First try to apply NBT data
		if (nbtData != null) {
			try {
				// Note: this method should not throw any exception. It should log directly to the console
				Bukkit.getUnsafe().modifyItemStack(itemStack, nbtData);
			} catch (Throwable t) {
				this.nbtData = null;
				Log.warning("Could not apply NBT-DATA to an item.", t);
			}
		}

		// Then apply data from config nodes, overwriting NBT data if there are confliting values
		ItemMeta itemMeta = itemStack.getItemMeta();

		if (hasName()) {
			itemMeta.setDisplayName(calculateName(viewer));
		}
		if (hasLore()) {
			itemMeta.setLore(calculateLore(viewer));
		}

		if (leatherColor != null && itemMeta instanceof LeatherArmorMeta) {
			((LeatherArmorMeta) itemMeta).setColor(leatherColor);
		}

		if (skullOwner != null && itemMeta instanceof SkullMeta) {
			String skullOwner = this.skullOwner.getValue(viewer);
			((SkullMeta) itemMeta).setOwner(skullOwner);
		}

		if (bannerColor != null && itemMeta instanceof BannerMeta) {
			BannerMeta bannerMeta = (BannerMeta) itemMeta;
			bannerMeta.setBaseColor(bannerColor);
			if (bannerPatterns != null) {
				((BannerMeta) itemMeta).setPatterns(bannerPatterns);
			}
		}
		
		// Hide all text details (damage, enchantments, potions, etc,)
		if (Utils.isNullOrEmpty(itemMeta.getItemFlags())) {
			itemMeta.addItemFlags(ItemFlag.values());
		}

		itemStack.setItemMeta(itemMeta);

		if (enchantments != null) {
			for (Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
				itemStack.addUnsafeEnchantment(entry.getKey(), entry.getValue());
			}
		}


		if (!this.hasVariables()) {
			// If there are no variables, cache the item
			cachedItem = itemStack;
		}

		return itemStack;
	}

	@Override
	public boolean onClick(Inventory inventory, Player clicker) {
		if (clickHandler == null) {
			return closeOnClick;
		}
		
		ClickResult result = clickHandler.onClick(clicker);
		switch (result) {
			case CLOSE: 
				return true;
			case KEEP_OPEN: 
				return false;
			default: 
				return closeOnClick;
		}
	}
}
