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
package me.filoghost.chestcommands.parsing.attribute;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.icon.requirement.RequiredItem;
import me.filoghost.chestcommands.parsing.ItemStackParser;
import me.filoghost.chestcommands.parsing.ParseException;

import java.util.ArrayList;
import java.util.List;

public class RequiredItemsAttribute implements ApplicableIconAttribute {

	private final List<RequiredItem> requiredItems;

	public RequiredItemsAttribute(List<String> serializedRequiredItems, AttributeErrorHandler errorHandler) {
		requiredItems = new ArrayList<>();

		for (String serializedItem : serializedRequiredItems) {
			try {
				ItemStackParser itemReader = new ItemStackParser(serializedItem, true);
				RequiredItem requiredItem = new RequiredItem(itemReader.getMaterial(), itemReader.getAmount());
				if (itemReader.hasExplicitDurability()) {
					requiredItem.setRestrictiveDurability(itemReader.getDurability());
				}
				requiredItems.add(requiredItem);
			} catch (ParseException e) {
				errorHandler.onListElementError(serializedItem, e);
			}
		}
	}
	
	@Override
	public void apply(InternalConfigurableIcon icon) {
		icon.setRequiredItems(requiredItems);
	}

}
