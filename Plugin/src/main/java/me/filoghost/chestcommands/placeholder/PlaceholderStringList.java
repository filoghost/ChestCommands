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
package me.filoghost.chestcommands.placeholder;

import com.google.common.collect.ImmutableList;
import me.filoghost.chestcommands.util.Preconditions;
import me.filoghost.chestcommands.util.collection.CollectionUtils;
import org.bukkit.entity.Player;

import java.util.List;

public class PlaceholderStringList {

	private final ImmutableList<String> originalList;
	private final ImmutableList<String> listWithStaticPlaceholders;
	private final ImmutableList<PlaceholderString> placeholderStringList;
	private final boolean hasDynamicPlaceholders;
	
	public PlaceholderStringList(List<String> list) {
		Preconditions.notNull(list, "list");
		this.originalList = ImmutableList.copyOf(list);

		// Replace static placeholders only once, if present
		if (PlaceholderManager.hasStaticPlaceholders(originalList)) {
			this.listWithStaticPlaceholders = CollectionUtils.transformImmutable(originalList, PlaceholderManager::replaceStaticPlaceholders);
		} else {
			this.listWithStaticPlaceholders = originalList;
		}

		this.hasDynamicPlaceholders = PlaceholderManager.hasRelativePlaceholders(listWithStaticPlaceholders);
		if (hasDynamicPlaceholders) {
			this.placeholderStringList = CollectionUtils.transformImmutable(listWithStaticPlaceholders, PlaceholderString::of);
		} else {
			this.placeholderStringList = null;
		}
	}
	
	public ImmutableList<String> getOriginalValue() {
		return originalList;
	}
	
	public ImmutableList<String> getValue(Player player) {
		if (hasDynamicPlaceholders) {
			return CollectionUtils.transformImmutable(placeholderStringList, element -> element.getValue(player));
		} else {
			return listWithStaticPlaceholders;
		}
	}
	
	public boolean hasDynamicPlaceholders() {
		return hasDynamicPlaceholders;
	}

}
