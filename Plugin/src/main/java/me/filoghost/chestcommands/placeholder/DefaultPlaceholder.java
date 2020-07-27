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
import me.filoghost.chestcommands.hook.VaultEconomyHook;
import org.bukkit.Bukkit;

public enum DefaultPlaceholder {

	PLAYER("player", (player, argument) -> player.getName()),

	ONLINE("online", (player, argument) -> String.valueOf(Bukkit.getOnlinePlayers().size())),

	MAX_PLAYERS("max_players", (player, argument) -> String.valueOf(Bukkit.getMaxPlayers())),

	WORLD("world", (player, argument) -> player.getWorld().getName()),

	MONEY("money", (player, argument) -> {
		if (VaultEconomyHook.INSTANCE.isEnabled()) {
			return VaultEconomyHook.formatMoney(VaultEconomyHook.getMoney(player));
		} else {
			return "[ECONOMY PLUGIN NOT FOUND]";
		}
	});


	private final String identifier;
	private final PlaceholderReplacer replacer;

	DefaultPlaceholder(String identifier, PlaceholderReplacer replacer) {
		this.identifier = identifier;
		this.replacer = replacer;
	}

	public String getIdentifier() {
		return identifier;
	}

	public PlaceholderReplacer getReplacer() {
		return replacer;
	}

}
