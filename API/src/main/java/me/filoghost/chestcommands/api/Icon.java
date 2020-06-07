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
package me.filoghost.chestcommands.api;

import java.util.List;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.filoghost.chestcommands.api.internal.BackendAPI;

public interface Icon {
	
	public static Icon create(Material material) {
		return BackendAPI.getImplementation().createIcon(material);
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

	boolean hasName();

	void setLore(String... lore);

	void setLore(List<String> lore);

	boolean hasLore();

	List<String> getLore();

	void setEnchantments(Map<Enchantment, Integer> enchantments);

	Map<Enchantment, Integer> getEnchantments();

	void addEnchantment(Enchantment ench);

	void addEnchantment(Enchantment ench, Integer level);

	void removeEnchantment(Enchantment ench);

	void clearEnchantments();

	Color getColor();

	void setColor(Color color);

	String getSkullOwner();

	void setSkullOwner(String skullOwner);

	DyeColor getBannerColor();

	void setBannerColor(DyeColor bannerColor);

	List<Pattern> getBannerPatterns();

	void setBannerPatterns(List<Pattern> bannerPatterns);

	void setCloseOnClick(boolean closeOnClick);

	void setClickHandler(ClickHandler clickHandler);

	ClickHandler getClickHandler();

    ItemStack createItemstack(Player viewer);

    boolean onClick(Player clicker);

}