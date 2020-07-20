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
import me.filoghost.chestcommands.util.collection.CollectionUtils;
import org.bukkit.entity.Player;

import java.util.List;

public class RelativeStringList {

	private final ImmutableList<String> originalList;
	private final List<RelativeString> relativeList;
	private final boolean hasPlaceholders;
	
	public RelativeStringList(List<String> list) {
		if (list != null) {
			this.originalList = ImmutableList.copyOf(list);
			this.relativeList = CollectionUtils.transform(list, RelativeString::of);
			this.hasPlaceholders = this.relativeList.stream().anyMatch(RelativeString::hasPlaceholders);
		} else {
			this.originalList = null;
			this.relativeList = null;
			this.hasPlaceholders = false;
		}
	}
	
	public ImmutableList<String> getRawValue() {
		return originalList;
	}
	
	public List<String> getValue(Player player) {
		if (hasPlaceholders) {
			return CollectionUtils.transform(relativeList, element -> element.getValue(player));
		} else {
			return originalList;
		}
	}
	
	public boolean hasPlaceholders() {
		return hasPlaceholders;
	}

}
