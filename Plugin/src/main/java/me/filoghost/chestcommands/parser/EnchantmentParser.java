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
package me.filoghost.chestcommands.parser;

import org.bukkit.enchantments.Enchantment;

import me.filoghost.chestcommands.util.Registry;
import me.filoghost.chestcommands.util.Strings;
import me.filoghost.chestcommands.util.ErrorCollector;

public class EnchantmentParser {

	private static Registry<Enchantment> ENCHANTMENTS_REGISTRY;

	static {
		ENCHANTMENTS_REGISTRY = Registry.fromValues(Enchantment.values(), Enchantment::getName);
		
		// Add aliases
		ENCHANTMENTS_REGISTRY.put("Protection", Enchantment.PROTECTION_ENVIRONMENTAL);
		ENCHANTMENTS_REGISTRY.put("Fire Protection", Enchantment.PROTECTION_FIRE);
		ENCHANTMENTS_REGISTRY.put("Feather Falling", Enchantment.PROTECTION_FALL);
		ENCHANTMENTS_REGISTRY.put("Blast Protection", Enchantment.PROTECTION_EXPLOSIONS);
		ENCHANTMENTS_REGISTRY.put("Projectile Protection", Enchantment.PROTECTION_PROJECTILE);
		ENCHANTMENTS_REGISTRY.put("Respiration", Enchantment.OXYGEN);
		ENCHANTMENTS_REGISTRY.put("Aqua Affinity", Enchantment.WATER_WORKER);
		ENCHANTMENTS_REGISTRY.put("Thorns", Enchantment.THORNS);
		ENCHANTMENTS_REGISTRY.put("Sharpness", Enchantment.DAMAGE_ALL);
		ENCHANTMENTS_REGISTRY.put("Smite", Enchantment.DAMAGE_UNDEAD);
		ENCHANTMENTS_REGISTRY.put("Bane Of Arthropods", Enchantment.DAMAGE_ARTHROPODS);
		ENCHANTMENTS_REGISTRY.put("Knockback", Enchantment.KNOCKBACK);
		ENCHANTMENTS_REGISTRY.put("Fire Aspect", Enchantment.FIRE_ASPECT);
		ENCHANTMENTS_REGISTRY.put("Looting", Enchantment.LOOT_BONUS_MOBS);
		ENCHANTMENTS_REGISTRY.put("Efficiency", Enchantment.DIG_SPEED);
		ENCHANTMENTS_REGISTRY.put("Silk Touch", Enchantment.SILK_TOUCH);
		ENCHANTMENTS_REGISTRY.put("Unbreaking", Enchantment.DURABILITY);
		ENCHANTMENTS_REGISTRY.put("Fortune", Enchantment.LOOT_BONUS_BLOCKS);
		ENCHANTMENTS_REGISTRY.put("Power", Enchantment.ARROW_DAMAGE);
		ENCHANTMENTS_REGISTRY.put("Punch", Enchantment.ARROW_KNOCKBACK);
		ENCHANTMENTS_REGISTRY.put("Flame", Enchantment.ARROW_FIRE);
		ENCHANTMENTS_REGISTRY.put("Infinity", Enchantment.ARROW_INFINITE);
		ENCHANTMENTS_REGISTRY.put("Lure", Enchantment.LURE);
		ENCHANTMENTS_REGISTRY.put("Luck Of The Sea", Enchantment.LUCK);
	}

	public static EnchantmentDetails parseEnchantment(String input, String iconName, String menuFileName, ErrorCollector errorCollector) {
		int level = 1;

		if (input.contains(",")) {
			String[] levelSplit = Strings.trimmedSplit(input, ",", 2);

			try {
				level = Integer.parseInt(levelSplit[1].trim());
			} catch (NumberFormatException ex) {
				errorCollector.addError("The icon \"" + iconName + "\" in the menu \"" + menuFileName + "\" has an invalid enchantment level: " + levelSplit[1]);
			}
			input = levelSplit[0];
		}

		Enchantment ench = ENCHANTMENTS_REGISTRY.find(input);

		if (ench == null) {
			errorCollector.addError("The icon \"" + iconName + "\" in the menu \"" + menuFileName + "\" has an invalid enchantment: " + input);
		} else {
			return new EnchantmentDetails(ench, level);
		}

		return null;
	}
	
	
	public static class EnchantmentDetails {
		
		private final Enchantment enchantment;
		private final int level;
		
		private EnchantmentDetails(Enchantment enchantment, int level) {
			this.enchantment = enchantment;
			this.level = level;
		}

		public Enchantment getEnchantment() {
			return enchantment;
		}

		public int getLevel() {
			return level;
		}
		
	}

}
