/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.parsing.EnchantmentParser;
import me.filoghost.chestcommands.parsing.ParseException;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
