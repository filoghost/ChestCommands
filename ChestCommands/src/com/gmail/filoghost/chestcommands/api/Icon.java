package com.gmail.filoghost.chestcommands.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.google.common.collect.Lists;

public class Icon {

	private Material material;
	private int amount;
	private short dataValue;
	
	private String name;
	private List<String> lore;
	private Map<Enchantment, Integer> enchantments;
	private Color color;
	
	private boolean closeOnClick;
	private ClickHandler clickHandler;
	
	public Icon() {
		closeOnClick = true;
		enchantments = new HashMap<Enchantment, Integer>();
	}
	
	public void setMaterial(Material material) {
		if (material == Material.AIR) material = null;
		this.material = material;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public void setAmount(int amount) {
		if 		(amount < 1)	amount = 1;
		else if (amount > 127) 	amount = 127;
		
		this.amount = amount;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setDataValue(short dataValue) {
		if (dataValue < 0) dataValue = 0;
		
		this.dataValue = dataValue;
	}
	
	public short getDataValue() {
		return dataValue;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean hasName() {
		return name != null;
	}
	
	public void setLore(String... lore) {
		if (lore != null) {
			this.lore = Arrays.asList(lore);
		}
	}
	
	public void setLore(List<String> lore) {
		this.lore = lore;
	}
	
	public boolean hasLore() {
		return lore != null && lore.size() > 0;
	}
	
	public List<String> getLore() {
		return lore;
	}
	
	public void setEnchantments(Map<Enchantment, Integer> enchantments) {
		if (enchantments == null) {
			clearEnchantments();
			return;
		}
		this.enchantments = enchantments;
	}
	
	public Map<Enchantment, Integer> getEnchantments() {
		return new HashMap<Enchantment, Integer>(enchantments);
	}
	
	public void addEnchantment(Enchantment ench) {
		addEnchantment(ench, 1);
	}
	
	public void addEnchantment(Enchantment ench, Integer level) {
		enchantments.put(ench, level);
	}
	
	public void removeEnchantment(Enchantment ench) {
		enchantments.remove(ench);
	}
	
	public void clearEnchantments() {
		enchantments.clear();
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setCloseOnClick(boolean closeOnClick) {
		this.closeOnClick = closeOnClick;
	}
	
	public boolean isCloseOnClick() {
		return closeOnClick;
	}
	
	public void setClickHandler(ClickHandler clickHandler) {
		this.clickHandler = clickHandler;
	}
	
	public ClickHandler getClickHandler() {
		return clickHandler;
	}
	
	protected String calculateName() {
		if (hasName()) {
			// TODO some magic
			return name;
		}
		
		return null;
	}

	protected List<String> calculateLore() {

		List<String> output = null;
		
		if (hasLore()) {
			
			output = Lists.newArrayList();
			// TODO some magic
			for (String line : lore) {
				output.add(line);
			}
		}
		
		if (material == null) {

			if (output == null) output = Lists.newArrayList();
			
			// Add an error message.
			output.add(ChatColor.RED + "(Invalid material)");
		}
		
		return output;
	}
	
	public ItemStack createItemstack() {
		
		// If the material is not set, display BEDROCK.
		ItemStack itemStack = (material != null) ? new ItemStack(material, amount, dataValue) : new ItemStack(Material.BEDROCK, amount);
		
		// Apply name, lore and color.
		ItemMeta itemMeta = itemStack.getItemMeta();
		
		itemMeta.setDisplayName(calculateName());
		itemMeta.setLore(calculateLore());
		
		if (color != null && itemMeta instanceof LeatherArmorMeta) {
			((LeatherArmorMeta) itemMeta).setColor(color);
		}
		
		itemStack.setItemMeta(itemMeta);
		
		// Apply enchants.
		if (enchantments.size() > 0) {
			for (Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
				itemStack.addUnsafeEnchantment(entry.getKey(), entry.getValue());
			}
		}
		
		return itemStack;
	}

	public void onClick(Player whoClicked) {
		if (clickHandler != null) {
			clickHandler.onClick(whoClicked);
		}
		
	}
}
