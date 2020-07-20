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

import me.filoghost.chestcommands.hook.PlaceholderAPIHook;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class PlaceholderManager {

	private static final Map<Player, Map<Placeholder, String>> cachedReplacements = new WeakHashMap<>();

	public static boolean hasPlaceholders(String text) {
		if(text == null) {
			return false;
		}

		for (Placeholder placeholder : DefaultPlaceholders.values()) {
			if (text.contains(placeholder.getPlaceholderText())) {
				return true;
			}
		}

		if (PlaceholderAPIHook.INSTANCE.isEnabled() && PlaceholderAPIHook.hasPlaceholders(text)) {
			return true;
		}

		return false;
	}

	public static String replacePlaceholders(String text, Player player) {
		if (text == null) {
			return null;
		}

		for (Placeholder placeholder : DefaultPlaceholders.values()) {
			if (text.contains(placeholder.getPlaceholderText())) {
				String replacement = getCachedReplacement(placeholder, player);
				text = text.replace(placeholder.getPlaceholderText(), replacement);
			}
		}

		if (PlaceholderAPIHook.INSTANCE.isEnabled()) {
			text = PlaceholderAPIHook.setPlaceholders(text, player);
		}

		return text;
	}

	private static String getCachedReplacement(Placeholder placeholder, Player player) {
		return cachedReplacements
				.computeIfAbsent(player, key -> new HashMap<>())
				.computeIfAbsent(placeholder, key -> placeholder.getReplacementText(player));
	}

	public static void onTick() {
		cachedReplacements.forEach((player, placeholderMap) -> placeholderMap.clear());
	}


}
