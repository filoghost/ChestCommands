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

import me.filoghost.chestcommands.hook.VaultEconomyHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.function.Function;

public enum DefaultPlaceholders implements Placeholder {

	PLAYER("{player}", player -> player.getName()),

	ONLINE("{online}", player -> String.valueOf(Bukkit.getOnlinePlayers().size())),

	MAX_PLAYERS("{max_players}", player -> String.valueOf(Bukkit.getMaxPlayers())),

	WORLD("{world}", player -> player.getWorld().getName()),

	MONEY("{money}", player -> {
		if (VaultEconomyHook.INSTANCE.isEnabled()) {
			return VaultEconomyHook.formatMoney(VaultEconomyHook.getMoney(player));
		} else {
			return "[ECONOMY PLUGIN NOT FOUND]";
		}
	});


	private final String text;
	private final Function<Player, String> getReplacementFunction;

	DefaultPlaceholders(String text, Function<Player, String> getReplacementFunction) {
		this.text = text;
		this.getReplacementFunction = getReplacementFunction;
	}

	@Override
	public String getPlaceholderText() {
		return text;
	}

	@Override
	public String getReplacementText(Player player) {
		return getReplacementFunction.apply(player);
	}

}
