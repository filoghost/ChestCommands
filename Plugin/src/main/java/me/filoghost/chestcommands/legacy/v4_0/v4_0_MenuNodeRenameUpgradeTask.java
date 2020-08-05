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
package me.filoghost.chestcommands.legacy.v4_0;

import me.filoghost.chestcommands.legacy.upgrade.RegexUpgradeTask;
import me.filoghost.chestcommands.parsing.icon.AttributeType;

import java.nio.file.Path;

public class v4_0_MenuNodeRenameUpgradeTask extends RegexUpgradeTask {

	public v4_0_MenuNodeRenameUpgradeTask(Path menuFile) {
		super(menuFile);
	}
	@Override
	protected void computeRegexChanges() {
		replaceSubNode("command", "commands");
		replaceSubNode("open-action", "open-actions");
		replaceSubNode("id", "material");

		replaceSubNode("ID", AttributeType.MATERIAL.getAttributeName());
		replaceSubNode("DATA-VALUE", AttributeType.DURABILITY.getAttributeName());
		replaceSubNode("NBT", AttributeType.NBT_DATA.getAttributeName());
		replaceSubNode("ENCHANTMENT", AttributeType.ENCHANTMENTS.getAttributeName());
		replaceSubNode("COMMAND", AttributeType.ACTIONS.getAttributeName());
		replaceSubNode("COMMANDS", AttributeType.ACTIONS.getAttributeName());
		replaceSubNode("REQUIRED-ITEM", AttributeType.REQUIRED_ITEMS.getAttributeName());
	}

}
