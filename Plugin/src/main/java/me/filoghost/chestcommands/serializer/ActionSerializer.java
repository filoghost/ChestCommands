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
package me.filoghost.chestcommands.serializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.filoghost.chestcommands.action.*;

public class ActionSerializer {

	private static Map<Pattern, IconCommandFactory> commandTypesMap = new HashMap<>();

	static {
		commandTypesMap.put(actionPattern("console:"), ConsoleCommandAction::new);
		commandTypesMap.put(actionPattern("op:"), OpCommandAction::new);
		commandTypesMap.put(actionPattern("(open|menu):"), OpenMenuAction::new);
		commandTypesMap.put(actionPattern("server:?"), ChangeServerAction::new); // The colon is optional
		commandTypesMap.put(actionPattern("tell:"), SendMessageAction::new);
		commandTypesMap.put(actionPattern("broadcast:"), BroadcastAction::new);
		commandTypesMap.put(actionPattern("give:"), GiveItemAction::new);
		commandTypesMap.put(actionPattern("give-?money:"), GiveMoneyAction::new);
		commandTypesMap.put(actionPattern("sound:"), PlaySoundAction::new);
		commandTypesMap.put(actionPattern("dragon-?bar:"), DragonBarAction::new);
	}

	private static Pattern actionPattern(String regex) {
		return Pattern.compile("^" + regex, Pattern.CASE_INSENSITIVE); // Case insensitive and only at the beginning
	}

	public static Action matchAction(String input) {
		for (Entry<Pattern, IconCommandFactory> entry : commandTypesMap.entrySet()) {
			Matcher matcher = entry.getKey().matcher(input);
			if (matcher.find()) {
				// Remove the action prefix and trim the spaces
				String cleanCommand = matcher.replaceFirst("").trim();
				return entry.getValue().create(cleanCommand);
			}
		}

		return new PlayerCommandAction(input); // Default action, no match found
	}
	
	
	public static interface IconCommandFactory {
		
		Action create(String actionString);
		
	}

}
