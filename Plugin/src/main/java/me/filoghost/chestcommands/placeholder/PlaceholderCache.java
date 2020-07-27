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

import me.filoghost.chestcommands.placeholder.scanner.PlaceholderMatch;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Supplier;

public class PlaceholderCache {

	private final Map<Player, Map<PlaceholderMatch, String>> cachedReplacements;

	public PlaceholderCache() {
		cachedReplacements = new WeakHashMap<>();
	}

	public String computeIfAbsent(PlaceholderMatch placeholderMatch, Player player, Supplier<String> replacementGetter) {
		return cachedReplacements
				.computeIfAbsent(player, key -> new HashMap<>())
				.computeIfAbsent(placeholderMatch, key -> replacementGetter.get());
	}

	public void onTick() {
		cachedReplacements.forEach((player, placeholderMap) -> placeholderMap.clear());
	}

}
