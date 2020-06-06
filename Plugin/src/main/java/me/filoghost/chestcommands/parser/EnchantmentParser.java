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

import java.util.HashMap;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;

import me.filoghost.chestcommands.util.ErrorCollector;
import me.filoghost.chestcommands.util.StringUtils;

public class EnchantmentParser {

	private static Map<String, Enchantment> enchantmentsMap = new HashMap<>();

	static {
		enchantmentsMap.put(formatLowercase("Protection"), Enchantment.PROTECTION_ENVIRONMENTAL);
		enchantmentsMap.put(formatLowercase("Fire Protection"), Enchantment.PROTECTION_FIRE);
		enchantmentsMap.put(formatLowercase("Feather Falling"), Enchantment.PROTECTION_FALL);
		enchantmentsMap.put(formatLowercase("Blast Protection"), Enchantment.PROTECTION_EXPLOSIONS);
		enchantmentsMap.put(formatLowercase("Projectile Protection"), Enchantment.PROTECTION_PROJECTILE);
		enchantmentsMap.put(formatLowercase("Respiration"), Enchantment.OXYGEN);
		enchantmentsMap.put(formatLowercase("Aqua Affinity"), Enchantment.WATER_WORKER);
		enchantmentsMap.put(formatLowercase("Thorns"), Enchantment.THORNS);
		enchantmentsMap.put(formatLowercase("Sharpness"), Enchantment.DAMAGE_ALL);
		enchantmentsMap.put(formatLowercase("Smite"), Enchantment.DAMAGE_UNDEAD);
		enchantmentsMap.put(formatLowercase("Bane Of Arthropods"), Enchantment.DAMAGE_ARTHROPODS);
		enchantmentsMap.put(formatLowercase("Knockback"), Enchantment.KNOCKBACK);
		enchantmentsMap.put(formatLowercase("Fire Aspect"), Enchantment.FIRE_ASPECT);
		enchantmentsMap.put(formatLowercase("Looting"), Enchantment.LOOT_BONUS_MOBS);
		enchantmentsMap.put(formatLowercase("Efficiency"), Enchantment.DIG_SPEED);
		enchantmentsMap.put(formatLowercase("Silk Touch"), Enchantment.SILK_TOUCH);
		enchantmentsMap.put(formatLowercase("Unbreaking"), Enchantment.DURABILITY);
		enchantmentsMap.put(formatLowercase("Fortune"), Enchantment.LOOT_BONUS_BLOCKS);
		enchantmentsMap.put(formatLowercase("Power"), Enchantment.ARROW_DAMAGE);
		enchantmentsMap.put(formatLowercase("Punch"), Enchantment.ARROW_KNOCKBACK);
		enchantmentsMap.put(formatLowercase("Flame"), Enchantment.ARROW_FIRE);
		enchantmentsMap.put(formatLowercase("Infinity"), Enchantment.ARROW_INFINITE);
		enchantmentsMap.put(formatLowercase("Lure"), Enchantment.LURE);
		enchantmentsMap.put(formatLowercase("Luck Of The Sea"), Enchantment.LUCK);

		for (Enchantment enchant : Enchantment.values()) {
			if (enchant != null) {
				// Accepts the ugly default names too
				enchantmentsMap.put(formatLowercase(enchant.getName()), enchant);
			}
		}
	}

	private static String formatLowercase(String string) {
		return StringUtils.stripChars(string, " _-").toLowerCase();
	}

	public static EnchantmentDetails parseEnchantment(String input, String iconName, String menuFileName, ErrorCollector errorCollector) {
		int level = 1;

		if (input.contains(",")) {
			String[] levelSplit = input.split(",");

			try {
				level = Integer.parseInt(levelSplit[1].trim());
			} catch (NumberFormatException ex) {
				errorCollector.addError("The icon \"" + iconName + "\" in the menu \"" + menuFileName + "\" has an invalid enchantment level: " + levelSplit[1]);
			}
			input = levelSplit[0];
		}

		Enchantment ench = matchEnchantment(input);

		if (ench == null) {
			errorCollector.addError("The icon \"" + iconName + "\" in the menu \"" + menuFileName + "\" has an invalid enchantment: " + input);
		} else {
			return new EnchantmentDetails(ench, level);
		}

		return null;
	}

	private static Enchantment matchEnchantment(String input) {
		if (input == null) {
			return null;
		}

		return enchantmentsMap.get(formatLowercase(input));
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
