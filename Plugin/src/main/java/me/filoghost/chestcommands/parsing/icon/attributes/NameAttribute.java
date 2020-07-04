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

public class NameAttribute implements ApplicableIconAttribute {

	private final String name;

	public NameAttribute(String name, AttributeErrorCollector attributeErrorCollector) {
		this.name = ChestCommands.getCustomPlaceholders().replaceAll(Colors.colorName(name));
	}
	
	@Override
	public void apply(InternalConfigurableIcon icon) {
		icon.setName(name);
	}

}