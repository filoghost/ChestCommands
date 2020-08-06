/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.chestcommands.icon;

import me.filoghost.chestcommands.api.Icon;
import me.filoghost.chestcommands.placeholder.PlaceholderString;
import me.filoghost.chestcommands.placeholder.PlaceholderStringList;
import me.filoghost.chestcommands.util.Preconditions;
import me.filoghost.chestcommands.util.collection.CollectionUtils;
import me.filoghost.chestcommands.util.logging.Log;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
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

public abstract class BaseConfigurableIcon implements Icon {

	private Material material;
	private int amount;
	private short durability;

	private String nbtData;
	private PlaceholderString name;
	private PlaceholderStringList lore;
	private Map<Enchantment, Integer> enchantments;
	private Color leatherColor;
	private PlaceholderString skullOwner;
	private DyeColor bannerColor;
	private List<Pattern> bannerPatterns;
	private boolean placeholdersEnabled;
	
	protected ItemStack cachedRendering; // Cache the rendered item when possible and if state hasn't changed

	public BaseConfigurableIcon(Material material) {
		Preconditions.checkArgumentNotAir(material, "material");
		this.material = material;
		this.amount = 1;
	}

	protected boolean shouldCacheRendering() {
		if (placeholdersEnabled) {
			return false;
		}

		return (name == null || !name.hasDynamicPlaceholders())
				&& (lore == null || !lore.hasDynamicPlaceholders())
				&& (skullOwner == null || !skullOwner.hasDynamicPlaceholders());
	}

	public void setMaterial(Material material) {
		Preconditions.checkArgumentNotAir(material, "material");
		this.material = material;
		cachedRendering = null;
	}

	public Material getMaterial() {
		return material;
	}

	public void setAmount(int amount) {
		Preconditions.checkArgument(amount >= 1, "Amount must 1 or greater");
		this.amount = Math.min(amount, 127);
		cachedRendering = null;
	}

	public int getAmount() {
		return amount;
	}

	public void setDurability(short durability) {
		Preconditions.checkArgument(durability >= 0, "Durability must not be negative");
		this.durability = durability;
		cachedRendering = null;
	}

	public short getDurability() {
		return durability;
	}

	public void setNBTData(String nbtData) {
		this.nbtData = nbtData;
		cachedRendering = null;
	}

	public String getNBTData() {
		return nbtData;
	}

	public void setName(String name) {
		this.name = PlaceholderString.of(name);
		cachedRendering = null;
	}

	public String getName() {
		if (name != null) {
			return name.getOriginalValue();
		} else {
			return null;
		}
	}

	public void setLore(String... lore) {
		if (lore != null) {
			setLore(Arrays.asList(lore));
		}
	}

	public void setLore(List<String> lore) {
		if (lore != null) {
			this.lore = new PlaceholderStringList(CollectionUtils.replaceNulls(lore, ""));
		} else {
			this.lore = null;
		}
		cachedRendering = null;
	}

	public List<String> getLore() {
		if (lore != null) {
			return new ArrayList<>(lore.getOriginalValue());
		} else {
			return null;
		}
	}

	public void setEnchantments(Map<Enchantment, Integer> enchantments) {
		this.enchantments = CollectionUtils.copy(enchantments);
		cachedRendering = null;
	}

	public Map<Enchantment, Integer> getEnchantments() {
		return CollectionUtils.copy(enchantments);
	}

	public void addEnchantment(Enchantment enchantment) {
		addEnchantment(enchantment, 1);
	}

	public void addEnchantment(Enchantment enchantment, Integer level) {
		if (enchantments == null) {
			enchantments = new HashMap<>();
		}
		enchantments.put(enchantment, level);
		cachedRendering = null;
	}

	public void removeEnchantment(Enchantment enchantment) {
		if (enchantments == null) {
			return;
		}
		enchantments.remove(enchantment);
		cachedRendering = null;
	}

	public Color getLeatherColor() {
		return leatherColor;
	}

	public void setLeatherColor(Color leatherColor) {
		this.leatherColor = leatherColor;
		cachedRendering = null;
	}

	public String getSkullOwner() {
		if (skullOwner != null) {
			return skullOwner.getOriginalValue();
		} else {
			return null;
		}
	}

	public void setSkullOwner(String skullOwner) {
		this.skullOwner = PlaceholderString.of(skullOwner);
		cachedRendering = null;
	}

	public DyeColor getBannerColor() {
		return bannerColor;
	}

	public void setBannerColor(DyeColor bannerColor) {
		this.bannerColor = bannerColor;
		cachedRendering = null;
	}

	public List<Pattern> getBannerPatterns() {
		return CollectionUtils.copy(bannerPatterns);
	}

	public void setBannerPatterns(List<Pattern> bannerPatterns) {
		this.bannerPatterns = CollectionUtils.copy(bannerPatterns);
		cachedRendering = null;
	}

	public void setPlaceholdersEnabled(boolean placeholdersEnabled) {
		this.placeholdersEnabled = placeholdersEnabled;
		cachedRendering = null;
	}

	public String renderName(Player viewer) {
		if (name == null) {
			return null;
		}
		if (!placeholdersEnabled) {
			return name.getOriginalValue();
		}

		String name = this.name.getValue(viewer);

		if (name.isEmpty()) {
			// Add a color to display the name empty
			return ChatColor.WHITE.toString();
		} else {
			return name;
		}
	}

	public List<String> renderLore(Player viewer) {
		if (lore == null) {
			return null;
		}
		if (!placeholdersEnabled) {
			return lore.getOriginalValue();
		}

		return lore.getValue(viewer);
	}

	@Override
	@SuppressWarnings("deprecation")
	public ItemStack render(Player viewer) {
		if (shouldCacheRendering() && cachedRendering != null) {
			// Performance: return a cached item
			return cachedRendering;
		}

		ItemStack itemStack = new ItemStack(material, amount, durability);

		// First try to apply NBT data
		if (nbtData != null) {
			try {
				// Note: this method should not throw any exception. It should log directly to the console
				Bukkit.getUnsafe().modifyItemStack(itemStack, nbtData);
			} catch (Throwable t) {
				this.nbtData = null;
				Log.warning("Could not apply NBT data to an item.", t);
			}
		}

		// Then apply data from config nodes, overwriting NBT data if there are conflicting values
		ItemMeta itemMeta = itemStack.getItemMeta();

		itemMeta.setDisplayName(renderName(viewer));
		itemMeta.setLore(renderLore(viewer));

		if (leatherColor != null && itemMeta instanceof LeatherArmorMeta) {
			((LeatherArmorMeta) itemMeta).setColor(leatherColor);
		}

		if (skullOwner != null && itemMeta instanceof SkullMeta) {
			String skullOwner = this.skullOwner.getValue(viewer);
			((SkullMeta) itemMeta).setOwner(skullOwner);
		}

		if (itemMeta instanceof BannerMeta) {
			BannerMeta bannerMeta = (BannerMeta) itemMeta;
			if (bannerColor != null) {
				bannerMeta.setBaseColor(bannerColor);
			}
			if (bannerPatterns != null) {
				((BannerMeta) itemMeta).setPatterns(bannerPatterns);
			}
		}
		
		// Hide all text details (damage, enchantments, potions, etc,)
		if (itemMeta.getItemFlags().isEmpty()) {
			itemMeta.addItemFlags(ItemFlag.values());
		}

		itemStack.setItemMeta(itemMeta);

		if (enchantments != null) {
			enchantments.forEach(itemStack::addUnsafeEnchantment);
		}


		if (shouldCacheRendering()) {
			cachedRendering = itemStack;
		}

		return itemStack;
	}

}
