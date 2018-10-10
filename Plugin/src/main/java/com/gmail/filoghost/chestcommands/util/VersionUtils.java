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
package com.gmail.filoghost.chestcommands.util;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

public class VersionUtils {
	
	private static boolean setup;
	private static Method oldGetOnlinePlayersMethod;
	private static boolean useReflection;
	
	public static Collection<? extends Player> getOnlinePlayers() {
		try {
			
			if (!setup) {
				oldGetOnlinePlayersMethod = Bukkit.class.getDeclaredMethod("getOnlinePlayers");
				if (oldGetOnlinePlayersMethod.getReturnType() == Player[].class) {
					useReflection = true;
				}
				
				setup = true;
			}
		
			if (!useReflection) {
				return Bukkit.getOnlinePlayers();
			} else {
				Player[] playersArray = (Player[]) oldGetOnlinePlayersMethod.invoke(null);
				return ImmutableList.copyOf(playersArray);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}
}
