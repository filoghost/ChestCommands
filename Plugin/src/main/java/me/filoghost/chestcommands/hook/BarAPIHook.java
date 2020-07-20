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
package me.filoghost.chestcommands.hook;

import me.confuser.barapi.BarAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public enum BarAPIHook implements PluginHook {

	INSTANCE;
	
	private boolean enabled;

	@Override
	public void setup() {
		enabled = Bukkit.getPluginManager().getPlugin("BarAPI") != null;
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	@SuppressWarnings("deprecation")
	public static void setMessage(Player player, String message, int seconds) {
		INSTANCE.checkEnabledState();
		
		BarAPI.setMessage(player, message, seconds);
	}

}
