/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.parsing.EnchantmentParser;
import me.filoghost.chestcommands.parsing.ParseException;
import org.bukkit.enchantments.Enchantment;

public class EnchantmentsAttribute implements IconAttribute {

    private final Map<Enchantment, Integer> enchantments;

    public EnchantmentsAttribute(List<String> serializedEnchantments, AttributeErrorHandler errorHandler) {
        enchantments = new HashMap<>();

        for (String serializedEnchantment : serializedEnchantments) {
            if (serializedEnchantment == null || serializedEnchantment.isEmpty()) {
                continue; // Skip
            }

            try {
                EnchantmentParser.EnchantmentDetails enchantment = EnchantmentParser.parseEnchantment(serializedEnchantment);
                enchantments.put(enchantment.getEnchantment(), enchantment.getLevel());
            } catch (ParseException e) {
                errorHandler.onListElementError(serializedEnchantment, e);
            }
        }
    }

    @Override
    public void apply(InternalConfigurableIcon icon) {
        icon.setEnchantments(enchantments);
    }

}
