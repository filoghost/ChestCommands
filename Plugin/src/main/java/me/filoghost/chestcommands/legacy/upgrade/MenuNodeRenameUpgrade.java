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
package me.filoghost.chestcommands.legacy.upgrade;

import me.filoghost.chestcommands.parsing.icon.IconSettingsNode;

import java.nio.file.Path;
import java.util.regex.Pattern;

public class MenuNodeRenameUpgrade extends RegexUpgrade {

	public MenuNodeRenameUpgrade(Path menuFile) {
		super(menuFile);

		addSubNodeReplacer("command", "commands");
		addSubNodeReplacer("open-action", "open-actions");
		addSubNodeReplacer("id", "material");

		addSubNodeReplacer("ID", IconSettingsNode.MATERIAL);
		addSubNodeReplacer("DATA-VALUE", IconSettingsNode.DURABILITY);
		addSubNodeReplacer("NBT", IconSettingsNode.NBT_DATA);
		addSubNodeReplacer("ENCHANTMENT", IconSettingsNode.ENCHANTMENTS);
		addSubNodeReplacer("COMMAND", IconSettingsNode.ACTIONS);
		addSubNodeReplacer("COMMANDS", IconSettingsNode.ACTIONS);
		addSubNodeReplacer("REQUIRED-ITEM", IconSettingsNode.REQUIRED_ITEMS);
	}

	private void addSubNodeReplacer(String oldNode, String newNode) {
		addRegexReplacer(
				Pattern.compile("(^\\s+)" + Pattern.quote(oldNode) + "(:)"),
				matcher -> matcher.group(1) + newNode + matcher.group(2));
	}

}
