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
package me.filoghost.chestcommands.parsing.icon.attributes;

import me.filoghost.chestcommands.action.Action;
import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.parsing.ActionParser;
import me.filoghost.chestcommands.parsing.icon.ApplicableIconAttribute;
import me.filoghost.chestcommands.parsing.icon.AttributeErrorCollector;

import java.util.ArrayList;
import java.util.List;

public class ActionsAttribute implements ApplicableIconAttribute {

	private final List<Action> actions;

	public ActionsAttribute(List<String> serializedActions, AttributeErrorCollector attributeErrorCollector) {
		actions = new ArrayList<>();

		for (String serializedAction : serializedActions) {
			if (serializedAction == null || serializedAction.isEmpty()) {
				continue; // Skip
			}

			actions.add(ActionParser.parseAction(serializedAction));
		}
	}
	
	@Override
	public void apply(InternalConfigurableIcon icon) {
		icon.setClickActions(actions);
	}

}
