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

import me.filoghost.chestcommands.internal.icon.IconCommand;
import me.filoghost.chestcommands.internal.icon.command.*;

public class CommandSerializer {

	private static Map<Pattern, IconCommandFactory> commandTypesMap = new HashMap<>();

	static {
		commandTypesMap.put(commandPattern("console:"), ConsoleIconCommand::new);
		commandTypesMap.put(commandPattern("op:"), OpIconCommand::new);
		commandTypesMap.put(commandPattern("(open|menu):"), OpenIconCommand::new);
		commandTypesMap.put(commandPattern("server:?"), ServerIconCommand::new); // The colon is optional
		commandTypesMap.put(commandPattern("tell:"), TellIconCommand::new);
		commandTypesMap.put(commandPattern("broadcast:"), BroadcastIconCommand::new);
		commandTypesMap.put(commandPattern("give:"), GiveIconCommand::new);
		commandTypesMap.put(commandPattern("give-?money:"), GiveMoneyIconCommand::new);
		commandTypesMap.put(commandPattern("sound:"), SoundIconCommand::new);
		commandTypesMap.put(commandPattern("dragon-?bar:"), DragonBarIconCommand::new);
	}

	private static Pattern commandPattern(String regex) {
		return Pattern.compile("^" + regex, Pattern.CASE_INSENSITIVE); // Case insensitive and only at the beginning
	}

	public static IconCommand matchCommand(String input) {
		for (Entry<Pattern, IconCommandFactory> entry : commandTypesMap.entrySet()) {
			Matcher matcher = entry.getKey().matcher(input);
			if (matcher.find()) {
				// Remove the command prefix and trim the spaces
				String cleanCommand = matcher.replaceFirst("").trim();
				return entry.getValue().create(cleanCommand);
			}
		}

		return new PlayerIconCommand(input); // Normal command, no match found
	}
	
	
	public static interface IconCommandFactory {
		
		IconCommand create(String commandString);
		
	}

}
