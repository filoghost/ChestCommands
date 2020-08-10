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
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.action.Action;
import me.filoghost.chestcommands.action.DisabledAction;
import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.logging.ErrorMessages;
import me.filoghost.chestcommands.parsing.ActionParser;
import me.filoghost.chestcommands.parsing.ParseException;

import java.util.ArrayList;
import java.util.List;

public class ActionsAttribute implements IconAttribute {

	private final List<Action> actions;

	public ActionsAttribute(List<String> serializedActions, AttributeErrorHandler errorHandler) {
		actions = new ArrayList<>();

		for (String serializedAction : serializedActions) {
			if (serializedAction == null || serializedAction.isEmpty()) {
				continue; // Skip
			}

			try {
				actions.add(ActionParser.parse(serializedAction));
			} catch (ParseException e) {
				actions.add(new DisabledAction(ErrorMessages.User.configurationError(
						"an action linked to clicking this icon was not executed because it was not valid")));
				errorHandler.onListElementError(serializedAction, e);
			}
		}
	}
	
	@Override
	public void apply(InternalConfigurableIcon icon) {
		icon.setClickActions(actions);
	}

}