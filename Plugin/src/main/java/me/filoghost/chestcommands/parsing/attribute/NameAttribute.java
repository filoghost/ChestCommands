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
package me.filoghost.chestcommands.parsing.attribute;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.util.Colors;
import org.bukkit.ChatColor;

public class NameAttribute implements ApplicableIconAttribute {

	private final String name;

	public NameAttribute(String name, AttributeErrorHandler errorHandler) {
		this.name = ChestCommands.getCustomPlaceholders().replacePlaceholders(colorName(name));
	}

	private String colorName(String input) {
		if (input == null || input.isEmpty()) {
			return input;
		}

		if (input.charAt(0) != ChatColor.COLOR_CHAR) {
			return ChestCommands.getSettings().default_color__name + Colors.addColors(input);
		} else {
			return Colors.addColors(input);
		}
	}

	@Override
	public void apply(InternalConfigurableIcon icon) {
		icon.setName(name);
	}

}
