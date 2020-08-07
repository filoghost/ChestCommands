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
package me.filoghost.chestcommands.parsing;

import me.filoghost.chestcommands.action.Action;
import me.filoghost.chestcommands.action.BroadcastAction;
import me.filoghost.chestcommands.action.ChangeServerAction;
import me.filoghost.chestcommands.action.ConsoleCommandAction;
import me.filoghost.chestcommands.action.DragonBarAction;
import me.filoghost.chestcommands.action.GiveItemAction;
import me.filoghost.chestcommands.action.GiveMoneyAction;
import me.filoghost.chestcommands.action.OpCommandAction;
import me.filoghost.chestcommands.action.OpenMenuAction;
import me.filoghost.chestcommands.action.PlaySoundAction;
import me.filoghost.chestcommands.action.PlayerCommandAction;
import me.filoghost.chestcommands.action.SendMessageAction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionParser {

	public static Action parse(String serializedAction) throws ParseException {
		for (ActionType actionType : ActionType.values()) {
			Matcher matcher = actionType.prefixPattern.matcher(serializedAction);
			if (matcher.find()) {
				// Remove the action prefix and trim the spaces
				serializedAction = matcher.replaceFirst("").trim();
				return actionType.actionFactory.create(serializedAction);
			}
		}

		return new PlayerCommandAction(serializedAction); // Default action, no match found
	}


	private enum ActionType {

		CONSOLE_COMMAND("console:", ConsoleCommandAction::new),
		OP_COMMAND("op:", OpCommandAction::new),
		OPEN("(open|menu):", OpenMenuAction::new),
		SERVER("server:?", ChangeServerAction::new), // The colon is optional
		TELL("tell:", SendMessageAction::new),
		BROADCAST("broadcast:", BroadcastAction::new),
		GIVE_ITEM("give:", GiveItemAction::new),
		GIVE_MONEY("give-?money:", GiveMoneyAction::new),
		SOUND("sound:", PlaySoundAction::new),
		BOSS_BAR("dragon-?bar:", DragonBarAction::new);


		private final Pattern prefixPattern;
		private final ActionFactory actionFactory;


		ActionType(String prefixPattern, ActionFactory actionFactory) {
			this.prefixPattern = Pattern.compile("^" + prefixPattern, Pattern.CASE_INSENSITIVE); // Case insensitive and only at the beginning
			this.actionFactory = actionFactory;
		}

		@FunctionalInterface
		private interface ActionFactory {

			Action create(String serializedAction) throws ParseException;

		}

	}
}
