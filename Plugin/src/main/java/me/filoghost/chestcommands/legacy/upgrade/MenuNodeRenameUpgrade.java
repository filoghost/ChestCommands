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
package me.filoghost.chestcommands.legacy.upgrade;

import me.filoghost.chestcommands.parsing.icon.IconSettingsNode;
import me.filoghost.chestcommands.parsing.menu.MenuSettingsNode;

import java.nio.file.Path;
import java.util.regex.Pattern;

public class MenuNodeRenameUpgrade extends RegexUpgrade {

	public MenuNodeRenameUpgrade(Path menuFile) {
		super(menuFile);

		addSubNodeReplacer("command", MenuSettingsNode.COMMANDS);
		addSubNodeReplacer("open-action", MenuSettingsNode.OPEN_ACTIONS);
		addSubNodeReplacer("open-with-item.id", MenuSettingsNode.OPEN_ITEM_MATERIAL);

		addSubNodeReplacer("ID", IconSettingsNode.MATERIAL);
		addSubNodeReplacer("DATA-VALUE", IconSettingsNode.DURABILITY);
		addSubNodeReplacer("NBT", IconSettingsNode.NBT_DATA);
		addSubNodeReplacer("ENCHANTMENT", IconSettingsNode.ENCHANTMENTS);
		addSubNodeReplacer("COMMAND", IconSettingsNode.ACTIONS);
		addSubNodeReplacer("COMMANDS", IconSettingsNode.ACTIONS);
		addSubNodeReplacer("REQUIRED-ITEM", IconSettingsNode.REQUIRED_ITEMS);
	}

	private void addSubNodeReplacer(String oldAttribute, String newAttribute) {
		addRegexReplacer(
				Pattern.compile("(^\\s+)" + Pattern.quote(oldAttribute) + "(:)"),
				matcher -> matcher.group(1) + newAttribute + matcher.group(2));
	}

}
