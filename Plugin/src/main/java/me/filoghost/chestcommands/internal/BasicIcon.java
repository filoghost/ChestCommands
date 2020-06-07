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
package me.filoghost.chestcommands.internal;

import org.bukkit.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.api.ClickHandler;
import me.filoghost.chestcommands.api.Icon;
import me.filoghost.chestcommands.variable.VariableManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

public class BasicIcon implements Icon {

	private Material material;
	private int amount;
	private short dataValue;

	private String nbtData;
	private String name;
	private List<String> lore;
	private Map<Enchantment, Integer> enchantments;
	private Color color;
	private String skullOwner;
	private DyeColor bannerColor;
	private List<Pattern> bannerPatterns;

	protected boolean closeOnClick;
	private ClickHandler clickHandler;

	private boolean nameHasVariables;
	private boolean[] loreLinesWithVariables;
	private boolean skullOwnerHasVariables;
	private ItemStack cachedItem; // When there are no variables, we don't recreate the item

	public BasicIcon() {
		enchantments = new HashMap<>();
		closeOnClick = true;
		amount = 1;        
	}

	public boolean hasVariables() {
		return nameHasVariables || loreLinesWithVariables != null || skullOwnerHasVariables;
	}

	@Override
	public void setMaterial(Material material) {
		if (material == Material.AIR) material = null;
		this.material = material;
	}

	@Override
	public Material getMaterial() {
		return material;
	}

	@Override
	public void setAmount(int amount) {
		if (amount < 1) amount = 1;
		else if (amount > 127) amount = 127;

		this.amount = amount;
	}

	@Override
	public int getAmount() {
		return amount;
	}

	@Override
	public void setDurability(short dataValue) {
		if (dataValue < 0) dataValue = 0;

		this.dataValue = dataValue;
	}

	@Override
	public short getDurability() {
		return dataValue;
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
		this.name = name;
		this.nameHasVariables = VariableManager.hasVariables(name);
	}

	@Override
	public boolean hasName() {
		return name != null;
	}

	@Override
	public void setLore(String... lore) {
		if (lore != null) {
			setLore(Arrays.asList(lore));
		}
	}

	@Override
	public void setLore(List<String> lore) {
		this.lore = lore;
		this.loreLinesWithVariables = null;

		if (lore != null) {
			for (int i = 0; i < lore.size(); i++) {
				if (VariableManager.hasVariables(lore.get(i))) {
					if (this.loreLinesWithVariables == null) {
						this.loreLinesWithVariables = new boolean[lore.size()];
					}
					loreLinesWithVariables[i] = true;
				}
			}
		}
	}

	@Override
	public boolean hasLore() {
		return lore != null && lore.size() > 0;
	}

	@Override
	public List<String> getLore() {
		return lore;
	}

	@Override
	public void setEnchantments(Map<Enchantment, Integer> enchantments) {
		if (enchantments == null) {
			this.enchantments.clear();
			return;
		}
		this.enchantments = enchantments;
	}

	@Override
	public Map<Enchantment, Integer> getEnchantments() {
		return new HashMap<>(enchantments);
	}

	@Override
	public void addEnchantment(Enchantment ench) {
		addEnchantment(ench, 1);
	}

	@Override
	public void addEnchantment(Enchantment ench, Integer level) {
		enchantments.put(ench, level);
	}

	@Override
	public void removeEnchantment(Enchantment ench) {
		enchantments.remove(ench);
	}

	@Override
	public void clearEnchantments() {
		enchantments.clear();
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public String getSkullOwner() {
		return skullOwner;
	}

	@Override
	public void setSkullOwner(String skullOwner) {
		this.skullOwner = skullOwner;
		this.skullOwnerHasVariables = VariableManager.hasVariables(skullOwner);
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
		return bannerPatterns;
	}

	@Override
	public void setBannerPatterns(List<Pattern> bannerPatterns) {
		this.bannerPatterns = bannerPatterns;
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

	protected String calculateName(Player viewer) {
		if (hasName()) {

			String name = this.name;

			if (viewer != null && nameHasVariables) {
				name = VariableManager.setVariables(name, viewer);
			}

			if (name.isEmpty()) {
				// Add a color to display the name empty
				return ChatColor.WHITE.toString();
			} else {
				return name;
			}
		}

		return null;
	}

	protected List<String> calculateLore(Player viewer) {

		List<String> output = null;

		if (hasLore()) {

			output = new ArrayList<>();

			if (viewer != null && loreLinesWithVariables != null) {
				for (int i = 0; i < lore.size(); i++) {
					String line = lore.get(i);
					if (loreLinesWithVariables[i]) {
						line = VariableManager.setVariables(line, viewer);
					}
					output.add(line);
				}
			} else {
				// Otherwise just copy the lines
				output.addAll(lore);
			}
		}

		if (material == null) {

			if (output == null) {
				output = new ArrayList<>();
			}

			// Add an error message
			output.add(ChatColor.RED + "(Invalid material)");
		}

		return output;
	}

	@SuppressWarnings("deprecation")
	public ItemStack createItemstack(Player viewer) {

		if (!this.hasVariables() && cachedItem != null) {
			// Performance
			return cachedItem;
		}

		// If the material is not set, display BEDROCK
		ItemStack itemStack = (material != null) ? new ItemStack(material, amount, dataValue) : new ItemStack(Material.BEDROCK, amount);

		// First try to apply NBT data
		if (nbtData != null) {
			try {
				// Note: this method should not throw any exception. It should log directly to the console
				Bukkit.getUnsafe().modifyItemStack(itemStack, nbtData);
			} catch (Throwable t) {
				this.nbtData = null;
				ChestCommands.getInstance().getLogger().log(Level.WARNING, "Could not apply NBT-DATA to an item.", t);
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

		if (color != null && itemMeta instanceof LeatherArmorMeta) {
			((LeatherArmorMeta) itemMeta).setColor(color);
		}

		if (skullOwner != null && itemMeta instanceof SkullMeta) {
			String skullOwner = this.skullOwner;
			if(skullOwnerHasVariables) {
				skullOwner = VariableManager.setVariables(skullOwner, viewer);
			}
			((SkullMeta) itemMeta).setOwner(skullOwner);
		}

		if (bannerColor != null && itemMeta instanceof BannerMeta) {
			BannerMeta bannerMeta = (BannerMeta) itemMeta;
			bannerMeta.setBaseColor(bannerColor);
			if (bannerPatterns != null) {
				((BannerMeta) itemMeta).setPatterns(bannerPatterns);
			}
		}

		itemStack.setItemMeta(itemMeta);

		if (enchantments.size() > 0) {
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

	public boolean onClick(Player whoClicked) {
		if (clickHandler != null) {
			return clickHandler.onClick(whoClicked);
		}

		return closeOnClick;
	}
}
