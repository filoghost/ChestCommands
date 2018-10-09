package com.gmail.filoghost.chestcommands.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.gmail.filoghost.chestcommands.internal.Variable;
import com.gmail.filoghost.chestcommands.util.Utils;

public class Icon {

	private Material material;
	private int amount;
	private short dataValue;
	
	private String name;
	private List<String> lore;
	private Map<Enchantment, Integer> enchantments;
	private Color color;
	private String skullOwner;
	
	protected boolean closeOnClick;
	private ClickHandler clickHandler;
	
	private Set<Variable> nameVariables;
	private Map<Integer, Set<Variable>> loreVariables;
	private ItemStack cachedItem; // When there are no variables, we don't recreate the item.
	
	public Icon() {
		enchantments = new HashMap<Enchantment, Integer>();
		closeOnClick = true;
	}
	
	public boolean hasVariables() {
		return nameVariables != null || loreVariables != null;
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
		this.nameVariables = null; // Reset the variables
		
		if (name != null) {
			for (Variable variable : Variable.values()) {
				if (name.contains(variable.getText())) {
					
					if (nameVariables == null) {
						nameVariables = new HashSet<Variable>();
					}
		
					nameVariables.add(variable);
				}
			}
		}
	}
	
	public boolean hasName() {
		return name != null;
	}
	
	public void setLore(String... lore) {
		if (lore != null) {
			setLore(Arrays.asList(lore));
		}
	}
	
	public void setLore(List<String> lore) {
		this.lore = lore;
		this.loreVariables = null; // Reset the variables
		
		if (lore != null) {
			for (int i = 0; i < lore.size(); i++) {
				for (Variable variable : Variable.values()) {
					if (lore.get(i).contains(variable.getText())) {
						
						if (loreVariables == null) {
							loreVariables = new HashMap<Integer, Set<Variable>>();
						}
						
						Set<Variable> lineVariables = loreVariables.get(i);
						
						if (lineVariables == null) {
							lineVariables = new HashSet<Variable>();
							loreVariables.put(i, lineVariables);
						}
						
						lineVariables.add(variable);
					}
				}
			}
		}
	}
	
	public boolean hasLore() {
		return lore != null && lore.size() > 0;
	}
	
	public List<String> getLore() {
		return lore;
	}
	
	public void setEnchantments(Map<Enchantment, Integer> enchantments) {
		if (enchantments == null) {
			this.enchantments.clear();
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

	public String getSkullOwner() {
		return skullOwner;
	}

	public void setSkullOwner(String skullOwner) {
		this.skullOwner = skullOwner;
	}

	public void setCloseOnClick(boolean closeOnClick) {
		this.closeOnClick = closeOnClick;
	}
	
	public void setClickHandler(ClickHandler clickHandler) {
		this.clickHandler = clickHandler;
	}
	
	public ClickHandler getClickHandler() {
		return clickHandler;
	}
	
	protected String calculateName(Player pov) {
		if (hasName()) {
			
			String name = this.name;
			
			if (pov != null && nameVariables != null) {
				for (Variable nameVariable : nameVariables) {
					name = name.replace(nameVariable.getText(), nameVariable.getReplacement(pov));
				}
			}

			if (name.isEmpty()) {
				// Add a color to display the name empty.
				return ChatColor.WHITE.toString();
			} else {
				return name;
			}
		}
		
		return null;
	}

	protected List<String> calculateLore(Player pov) {

		List<String> output = null;
		
		if (hasLore()) {
			
			output = Utils.newArrayList();

			if (pov != null && loreVariables != null) {
				for (int i = 0; i < lore.size(); i++) {
					
					String line = lore.get(i);
					
					Set<Variable> lineVariables = loreVariables.get(i);
					if (lineVariables != null) {
						for (Variable lineVariable : lineVariables) {
							line = line.replace(lineVariable.getText(), lineVariable.getReplacement(pov));
						}
					}
					
					output.add(line);
				}
			} else {
				// Otherwise just copy the lines.
				output.addAll(lore);
			}
		}
		
		if (material == null) {

			if (output == null) {
				output = Utils.newArrayList();
			}
			
			// Add an error message.
			output.add(ChatColor.RED + "(Invalid material)");
		}
		
		return output;
	}
	
	public ItemStack createItemstack(Player pov) {
		
		if (!this.hasVariables() && cachedItem != null) {
			// Performance.
			return cachedItem;
		}
		
		// If the material is not set, display BEDROCK.
		ItemStack itemStack = (material != null) ? new ItemStack(material, amount, dataValue) : new ItemStack(Material.BEDROCK, amount);
		
		// Apply name, lore and color.
		ItemMeta itemMeta = itemStack.getItemMeta();
		
		itemMeta.setDisplayName(calculateName(pov));
		itemMeta.setLore(calculateLore(pov));
		
		if (color != null && itemMeta instanceof LeatherArmorMeta) {
			((LeatherArmorMeta) itemMeta).setColor(color);
		}
		
		if (skullOwner != null && itemMeta instanceof SkullMeta) {
			((SkullMeta) itemMeta).setOwner(skullOwner);
		}
		
		itemStack.setItemMeta(itemMeta);
		
		// Apply enchants.
		if (enchantments.size() > 0) {
			for (Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
				itemStack.addUnsafeEnchantment(entry.getKey(), entry.getValue());
			}
		}
		
		if (!this.hasVariables()) {
			// If there are no variables, cache the item.
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
