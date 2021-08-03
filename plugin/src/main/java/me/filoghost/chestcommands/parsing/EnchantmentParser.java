/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.parsing;

import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.fcommons.Strings;
import me.filoghost.fcommons.collection.LookupRegistry;
import org.bukkit.enchantments.Enchantment;

public class EnchantmentParser {

    private static final LookupRegistry<Enchantment> ENCHANTMENTS_REGISTRY;

    static {
        ENCHANTMENTS_REGISTRY = LookupRegistry.fromValues(Enchantment.values(), Enchantment::getName);

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

    public static EnchantmentDetails parseEnchantment(String input) throws ParseException {
        int level = 1;

        if (input.contains(",")) {
            String[] levelSplit = Strings.splitAndTrim(input, ",", 2);

            try {
                level = NumberParser.getStrictlyPositiveInteger(levelSplit[1]);
            } catch (ParseException e) {
                throw new ParseException(Errors.Parsing.invalidEnchantmentLevel(levelSplit[1]), e);
            }
            input = levelSplit[0];
        }

        Enchantment enchantment = ENCHANTMENTS_REGISTRY.lookup(input);

        if (enchantment != null) {
            return new EnchantmentDetails(enchantment, level);
        } else {
            throw new ParseException(Errors.Parsing.unknownEnchantmentType(input));
        }
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
