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

import me.filoghost.chestcommands.ChestCommands;
import org.bukkit.entity.Player;

public class PlaceholderString {

	private final String originalString;
	private final String stringWithStaticPlaceholders;
	private final boolean hasDynamicPlaceholders;

	public static PlaceholderString of(String string) {
		if (string != null) {
			return new PlaceholderString(string);
		} else {
			return null;
		}
	}
	
	private PlaceholderString(String originalString) {
		this.originalString = originalString;
		this.stringWithStaticPlaceholders = ChestCommands.getCustomPlaceholders().replacePlaceholders(originalString);
		this.hasDynamicPlaceholders = PlaceholderManager.hasPlaceholders(stringWithStaticPlaceholders);
	}
	
	public String getValue(Player player) {
		if (hasDynamicPlaceholders) {
			return PlaceholderManager.replacePlaceholders(stringWithStaticPlaceholders, player);
		} else {
			return stringWithStaticPlaceholders;
		}
	}

	public String getOriginalValue() {
		return originalString;
	}

	public boolean hasDynamicPlaceholders() {
		return hasDynamicPlaceholders;
	}

}
