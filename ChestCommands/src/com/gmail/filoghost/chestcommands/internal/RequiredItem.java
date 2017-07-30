package com.gmail.filoghost.chestcommands.internal;

import com.gmail.filoghost.chestcommands.ChestCommands;
import com.gmail.filoghost.chestcommands.util.*;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.*;

import java.util.*;

public class RequiredItem implements ConfigurationSerializable {
    private Material material;
    private int amount;
    private short dataValue;
    private String displayName;
    private List<String> lore;
    private boolean isDurabilityRestrictive = false;
    private boolean isLoreExact = true;

    private RequiredItem(Material mat, int amount) {
        this.material = mat;
        this.amount = amount;
    }

    public RequiredItem(Material mat, int amount, String displayName) {
        this(mat, amount);
        if(displayName == null || displayName.isEmpty()) displayName = null;
        else this.displayName = ChatColor.translateAlternateColorCodes('&', displayName);
        this.isLoreExact = false;
        this.lore = null;
    }

    public RequiredItem(Material mat, int amount, String displayName, boolean exactLore, String... lore) {
        this(mat, amount, displayName);
        this.isLoreExact = exactLore;
        if(lore == null || lore.length == 0) {
            this.lore = null;
        } else {
            List<String> list = Utils.newArrayList();
            for(String s : lore) {
                if(s == null) list.add(s);
                else {
                    String c = ChatColor.translateAlternateColorCodes('&', s);
                    list.add(c);
                }
            } 
            this.lore = list;
        }
    }

    @SuppressWarnings("unchecked")
    public RequiredItem(Map<String, Object> map) {
        String mat = (String) map.get("material");
        Material m = Material.getMaterial(mat);
        this.material = m;
        this.amount = (int) map.get("amount");
        this.dataValue = (short) ((int) map.get("durability"));
        this.isDurabilityRestrictive = (boolean) map.get("restrictive durability");
        this.isLoreExact = (boolean) map.get("exact lore");
        
        String disp = (String) map.get("name");
        if(disp == null || disp.isEmpty()) this.displayName = null;
        else {
            this.displayName = ChatColor.translateAlternateColorCodes('&', disp);
        }
        
        List<String> lore = (List<String>) map.get("lore");
        if(lore == null || lore.isEmpty()) this.lore = null;
        else {
            List<String> list = Utils.newArrayList();
            for(String s : lore) {
                if(s == null) list.add(s);
                else {
                    String c = ChatColor.translateAlternateColorCodes('&', s);
                    list.add(c);
                }
            } 
            this.lore = list;
        }
    }

    public ItemStack createItemStack() {
        ItemStack is = new ItemStack(material, amount, dataValue);
        if(is != null) {
            ItemMeta meta = is.getItemMeta();
            if(displayName != null) meta.setDisplayName(displayName);
            if(lore != null) meta.setLore(lore);
            is.setItemMeta(meta);
        } return is;
    }

    public Material getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    public short getDataValue() {
        return dataValue;
    }
    
    public String getDisplayName() {
        if(displayName == null) return ChestCommands.getLang().any;
        else return displayName;
    }
    
    public String getDisplayLore() {
        if(lore == null) return ChestCommands.getLang().any;
        else {
            StringBuilder sb = new StringBuilder("\u00a7r[");
            for(int i = 0; i < lore.size(); i++) {
                if(i > 0) sb.append(", ");
                String s = lore.get(i);
                sb.append(s);
            }
            sb.append("\u00a7r]");
            return sb.toString();
        }
    }

    public void setRestrictiveDataValue(short data) {
        Validate.isTrue(data >= 0, "Data value cannot be negative");

        this.dataValue = data;
        isDurabilityRestrictive = true;
    }

    public boolean hasRestrictiveDataValue() {
        return isDurabilityRestrictive;
    }

    public boolean isValidDataValue(short data) {
        if (!isDurabilityRestrictive) return true;
        return data == this.dataValue;
    }

    public boolean hasItem(Player player) {
        int amountFound = 0;

        for (ItemStack item : player.getInventory().getContents()) {
            boolean found = false;
            if (item != null && isValidDataValue(item.getDurability())) {
                if(material != null) {
                    Material mat = item.getType();
                    found = (mat == material);
                } else found = (material == null);

                ItemMeta meta = item.getItemMeta();
                if(found) {
                    if(meta.hasDisplayName()) {
                        if(displayName == null) found = true;
                        else {
                            String disp = meta.getDisplayName();
                            found = disp.equals(displayName);
                        }
                    } else found = (displayName == null);
                }

                if(found) {
                    if(meta.hasLore()) {
                        List<String> mlore = meta.getLore();
                        if(isLoreExact) found = (lore.equals(mlore));
                        else {
                            for(String s : lore) {
                                if(!found) break;
                                found = mlore.contains(s);
                            }
                        }
                    } else found = (lore == null);
                }
            }

            if(found) amountFound += item.getAmount();
        }

        return amountFound >= amount;
    }

    public boolean takeItem(Player player) {
        if (amount <= 0) {
            return true;
        }

        int itemsToTake = amount; //start from amount and decrease

        ItemStack[] contents = player.getInventory().getContents();
        ItemStack current = null;


        for (int i = 0; i < contents.length; i++) {

            current = contents[i];
            if (current != null && current.getType() == material && isValidDataValue(current.getDurability())) {
                boolean found = false;
                if(displayName != null) {
                    ItemMeta meta = current.getItemMeta();
                    if(meta.hasDisplayName()) {
                        String disp = meta.getDisplayName();
                        found = disp.equals(displayName);
                    } else found = false;
                } else found = true;

                if(found) {
                    if(lore != null) {
                        ItemMeta meta = current.getItemMeta();
                        if(meta.hasLore()) {
                            List<String> ilore = meta.getLore();
                            found = lore.equals(ilore);
                        } else found = false;
                    } else found = true;
                }
                
                if(found) {
                    if (current.getAmount() > itemsToTake) {
                        current.setAmount(current.getAmount() - itemsToTake);
                        return true;
                    } else {
                        itemsToTake -= current.getAmount();
                        player.getInventory().setItem(i, new ItemStack(Material.AIR));
                    }
                }
            }

            // The end
            if (itemsToTake <= 0) return true;
        }

        return false;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = Utils.newHashMap();
        map.put("material", material.name());
        map.put("amount", amount);
        map.put("durability", dataValue);
        map.put("restrictive durability", isDurabilityRestrictive);
        map.put("name", displayName);
        map.put("lore", lore);
        return map;
    }

    @SuppressWarnings("deprecation")
    public String getID() {
        if(material == null) return ChestCommands.getLang().any;
        else {
            int id = material.getId();
            String s = Integer.toString(id);
            return s;
        }
    }
}
