package com.gmail.filoghost.chestcommands.serializer;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;

import com.gmail.filoghost.chestcommands.util.ErrorLogger;
import com.gmail.filoghost.chestcommands.util.StringUtils;

public class EnchantmentSerializer {
	
	private static Map<String, Enchantment> enchantmentsMap = new HashMap<String, Enchantment>();
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
				// Accepts the ugly default names too.
				enchantmentsMap.put(formatLowercase(enchant.getName()), enchant);
			}
		}
	}

	private static String formatLowercase(String string) {
		return StringUtils.stripChars(string, " _-").toLowerCase();
	}

	public static Map<Enchantment, Integer> loadEnchantments(String input, String iconName, String menuFileName, ErrorLogger errorLogger) {
		Map<Enchantment, Integer> output = new HashMap<Enchantment, Integer>();
		
		if (input == null || input.isEmpty()) {
			return output;
		}
		
		for (String singleEnchant : input.split(";")) {
			
			int level = 1;
			
			if (singleEnchant.contains(",")) {
				String[] levelSplit = singleEnchant.split(",");
				
				try {
					level = Integer.parseInt(levelSplit[1].trim());
				} catch (NumberFormatException ex) {
					errorLogger.addError("The icon \"" + iconName + "\" in the menu \"" + menuFileName + "\" has an invalid enchantment level: " + levelSplit[1]);
				}
				singleEnchant = levelSplit[0];
			}
			
			Enchantment ench = matchEnchantment(singleEnchant);
			
			if (ench == null) {
				errorLogger.addError("The icon \"" + iconName + "\" in the menu \"" + menuFileName + "\" has an invalid enchantment: " + singleEnchant);
			} else {
				output.put(ench, level);
			}
		}
		
		return output;
	}
	
	public static Enchantment matchEnchantment(String input) {
		if (input == null) {
			return null;
		}
		
		return enchantmentsMap.get(formatLowercase(input));
	}
	
}
