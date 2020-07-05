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

public class CachedGetters {

	private static long lastOnlinePlayersRefresh;
	private static int onlinePlayers;


	public static int getOnlinePlayers() {
		long now = System.currentTimeMillis();
		if (lastOnlinePlayersRefresh == 0 || now - lastOnlinePlayersRefresh > 1000) {
			// getOnlinePlayers() could be expensive if called frequently
			lastOnlinePlayersRefresh = now;
			onlinePlayers = Bukkit.getOnlinePlayers().size();
		}

		return onlinePlayers;
	}

}
