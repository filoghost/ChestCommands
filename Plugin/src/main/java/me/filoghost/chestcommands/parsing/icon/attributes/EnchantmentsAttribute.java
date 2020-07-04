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
package me.filoghost.chestcommands.parsing.icon.attributes;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.parsing.EnchantmentParser;
import me.filoghost.chestcommands.parsing.ParseException;
import me.filoghost.chestcommands.parsing.icon.ApplicableIconAttribute;
import me.filoghost.chestcommands.parsing.icon.AttributeErrorCollector;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantmentsAttribute implements ApplicableIconAttribute {

	private final Map<Enchantment, Integer> enchantments;

	public EnchantmentsAttribute(List<String> serializedEnchantments, AttributeErrorCollector attributeErrorCollector) {
		enchantments = new HashMap<>();

		for (String serializedEnchantment : serializedEnchantments) {
			if (serializedEnchantment == null || serializedEnchantment.isEmpty()) {
				continue; // Skip
			}

			try {
				EnchantmentParser.EnchantmentDetails enchantment = EnchantmentParser.parseEnchantment(serializedEnchantment);
				enchantments.put(enchantment.getEnchantment(), enchantment.getLevel());
			} catch (ParseException e) {
				attributeErrorCollector.addListElementError(serializedEnchantment, e);
			}
		}
	}
	
	@Override
	public void apply(InternalConfigurableIcon icon) {
		icon.setEnchantments(enchantments);
	}

}
