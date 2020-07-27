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

import me.filoghost.chestcommands.api.PlaceholderReplacer;
import me.filoghost.chestcommands.placeholder.scanner.PlaceholderMatch;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PlaceholderRegistry {

	private final Map<String, PlaceholderReplacer> internalPlaceholders = new HashMap<>();
	private final Map<String, Map<String, PlaceholderReplacer>> externalPlaceholders = new HashMap<>();

	public void registerInternalPlaceholder(String identifier, PlaceholderReplacer replacer) {
		internalPlaceholders.put(identifier, replacer);
	}

	public void registerExternalPlaceholder(Plugin plugin, String identifier, PlaceholderReplacer placeholderReplacer) {
		externalPlaceholders
				.computeIfAbsent(identifier, key -> new LinkedHashMap<>())
				.put(plugin.getName(), placeholderReplacer);
	}

	public PlaceholderReplacer getPlaceholderReplacer(PlaceholderMatch placeholderMatch) {
		if (placeholderMatch.getPluginNamespace() == null) {
			PlaceholderReplacer internalReplacer = internalPlaceholders.get(placeholderMatch.getIdentifier());
			if (internalReplacer != null) {
				return internalReplacer;
			}
		}

		Map<String, PlaceholderReplacer> externalReplacers = externalPlaceholders.get(placeholderMatch.getIdentifier());

		// Find exact replacer if plugin name is specified
		if (placeholderMatch.getPluginNamespace() != null) {
			return externalReplacers.get(placeholderMatch.getPluginNamespace());
		}

		if (externalReplacers != null && !externalReplacers.isEmpty()) {
			return externalReplacers.values().iterator().next();
		}

		return null;
	}

}
