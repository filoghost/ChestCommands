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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionParser {

	private static final Map<Pattern, ActionFactory> actionsByPrefix = new HashMap<>();
	static {
		addActionParserMapping("console:", ConsoleCommandAction::new);
		addActionParserMapping("op:", OpCommandAction::new);
		addActionParserMapping("(open|menu):", OpenMenuAction::new);
		addActionParserMapping("server:?", ChangeServerAction::new); // The colon is optional
		addActionParserMapping("tell:", SendMessageAction::new);
		addActionParserMapping("broadcast:", BroadcastAction::new);
		addActionParserMapping("give:", GiveItemAction::new);
		addActionParserMapping("give-?money:", GiveMoneyAction::new);
		addActionParserMapping("sound:", PlaySoundAction::new);
		addActionParserMapping("dragon-?bar:", DragonBarAction::new);
	}

	private static void addActionParserMapping(String regex, ActionFactory actionFactory) {
		actionsByPrefix.put(
				Pattern.compile("^" + regex, Pattern.CASE_INSENSITIVE), // Case insensitive and only at the beginning
				actionFactory);
	}

	public static Action parseAction(String input) {
		for (Entry<Pattern, ActionFactory> entry : actionsByPrefix.entrySet()) {
			Matcher matcher = entry.getKey().matcher(input);
			if (matcher.find()) {
				// Remove the action prefix and trim the spaces
				String serializedAction = matcher.replaceFirst("").trim();
				return entry.getValue().create(serializedAction);
			}
		}

		return new PlayerCommandAction(input); // Default action, no match found
	}
	
	@FunctionalInterface
	private interface ActionFactory {
		
		Action create(String actionString);
		
	}

}
