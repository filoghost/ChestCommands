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
package me.filoghost.chestcommands.parsing.icon.attributes;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.parsing.icon.ApplicableIconAttribute;
import me.filoghost.chestcommands.parsing.icon.AttributeErrorCollector;
import me.filoghost.chestcommands.util.Colors;
import me.filoghost.chestcommands.util.collection.CollectionUtils;
import org.bukkit.ChatColor;

import java.util.List;

public class LoreAttribute implements ApplicableIconAttribute {

	private final List<String> lore;
	
	public LoreAttribute(List<String> lore, AttributeErrorCollector attributeErrorCollector) {
		this.lore = ChestCommands.getCustomPlaceholders().replaceAll(colorLore(lore));
	}

	private List<String> colorLore(List<String> input) {
		return CollectionUtils.transform(input, line -> {
			if (line.isEmpty()) {
				return line;
			} else if (line.charAt(0) != ChatColor.COLOR_CHAR) {
				return ChestCommands.getSettings().default_color__lore + Colors.addColors(line);
			} else {
				return Colors.addColors(line);
			}
		});
	}

	@Override
	public void apply(InternalConfigurableIcon icon) {
		icon.setLore(lore);
	}

}
