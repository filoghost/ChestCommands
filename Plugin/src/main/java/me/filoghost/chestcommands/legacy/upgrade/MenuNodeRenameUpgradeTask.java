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

import me.filoghost.chestcommands.parsing.icon.AttributeType;

import java.nio.file.Path;
import java.util.regex.Pattern;

public class MenuNodeRenameUpgradeTask extends RegexUpgradeTask {

	public MenuNodeRenameUpgradeTask(Path menuFile) {
		super(menuFile);

		addSubNodeReplacer("command", "commands");
		addSubNodeReplacer("open-action", "open-actions");
		addSubNodeReplacer("id", "material");

		addSubNodeReplacer("ID", AttributeType.MATERIAL.getAttributeName());
		addSubNodeReplacer("DATA-VALUE", AttributeType.DURABILITY.getAttributeName());
		addSubNodeReplacer("NBT", AttributeType.NBT_DATA.getAttributeName());
		addSubNodeReplacer("ENCHANTMENT", AttributeType.ENCHANTMENTS.getAttributeName());
		addSubNodeReplacer("COMMAND", AttributeType.ACTIONS.getAttributeName());
		addSubNodeReplacer("COMMANDS", AttributeType.ACTIONS.getAttributeName());
		addSubNodeReplacer("REQUIRED-ITEM", AttributeType.REQUIRED_ITEMS.getAttributeName());
	}

	private void addSubNodeReplacer(String oldNode, String newNode) {
		addRegexReplacer(
				Pattern.compile("(^\\s+)" + Pattern.quote(oldNode) + "(:)"),
				matcher -> matcher.group(1) + newNode + matcher.group(2));
	}

}
