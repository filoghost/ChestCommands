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
package me.filoghost.chestcommands.placeholder;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.filoghost.chestcommands.hook.VaultEconomyHook;

public enum Placeholder {

	PLAYER("{player}") {
		@Override
		public String getReplacement(Player executor) {
			return executor.getName();
		}
	},

	ONLINE("{online}") {
		@Override
		public String getReplacement(Player executor) {
			return String.valueOf(CachedGetters.getOnlinePlayers());
		}
	},

	MAX_PLAYERS("{max_players}") {
		@Override
		public String getReplacement(Player executor) {
			return String.valueOf(Bukkit.getMaxPlayers());
		}
	},

	MONEY("{money}") {
		@Override
		public String getReplacement(Player executor) {
			if (VaultEconomyHook.INSTANCE.isEnabled()) {
				return VaultEconomyHook.formatMoney(VaultEconomyHook.getMoney(executor));
			} else {
				return "[ECONOMY PLUGIN NOT FOUND]";
			}
		}
	},

	WORLD("{world}") {
		@Override
		public String getReplacement(Player executor) {
			return executor.getWorld().getName();
		}
	};

	private final String text;

	Placeholder(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public abstract String getReplacement(Player executor);
}
