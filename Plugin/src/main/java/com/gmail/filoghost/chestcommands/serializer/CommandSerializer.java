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
package com.gmail.filoghost.chestcommands.serializer;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gmail.filoghost.chestcommands.ChestCommands;
import com.gmail.filoghost.chestcommands.internal.icon.IconCommand;
import com.gmail.filoghost.chestcommands.internal.icon.command.*;
import com.gmail.filoghost.chestcommands.util.ErrorLogger;
import com.gmail.filoghost.chestcommands.util.Utils;

public class CommandSerializer {

	private static Map<Pattern, Class<? extends IconCommand>> commandTypesMap = Utils.newHashMap();
	static {
		commandTypesMap.put(commandPattern("console:"), ConsoleIconCommand.class);
		commandTypesMap.put(commandPattern("op:"), OpIconCommand.class);
		commandTypesMap.put(commandPattern("(open|menu):"), OpenIconCommand.class);
		commandTypesMap.put(commandPattern("server:?"), ServerIconCommand.class); // The colon is optional.
		commandTypesMap.put(commandPattern("tell:"), TellIconCommand.class);
		commandTypesMap.put(commandPattern("broadcast:"), BroadcastIconCommand.class);
		commandTypesMap.put(commandPattern("give:"), GiveIconCommand.class);
		commandTypesMap.put(commandPattern("give-?money:"), GiveMoneyIconCommand.class);
		commandTypesMap.put(commandPattern("give-?coins:"), GiveCoinsIconCommand.class);
		commandTypesMap.put(commandPattern("sound:"), SoundIconCommand.class);
		commandTypesMap.put(commandPattern("dragon-?bar:"), DragonBarIconCommand.class);
	}
	
	private static Pattern commandPattern(String regex) {
		return Pattern.compile("^(?i)" + regex); // Case insensitive and only at the beginning.
	}
	
	public static void checkClassConstructors(ErrorLogger errorLogger) {
		for (Class<? extends IconCommand> clazz : commandTypesMap.values()) {
			try {
				clazz.getDeclaredConstructor(String.class).newInstance("");
			} catch (Exception ex) {
				String className = clazz.getName().replace("Command", "");
				className = className.substring(className.lastIndexOf('.') + 1, className.length());
				errorLogger.addError("Unable to register the \"" + className + "\" command type(" + ex.getClass().getName() + "), please inform the developer (filoghost). The plugin will still work, but all the \"" + className + "\" commands will be treated as normal commands.");
			}
		}
	}
	
	public static List<IconCommand> readCommands(String input) {
		String separator = ChestCommands.getSettings().multiple_commands_separator;
		if (separator == null || separator.length() == 0) {
			separator = ";";
		}
		
		String[] split = input.split(Pattern.quote(separator));
		List<IconCommand> iconCommands = Utils.newArrayList();
			
		for (String command : split) {
			String trim = command.trim();
				
			if (trim.length() > 0) {
				iconCommands.add(matchCommand(trim));
			}
		}
			
		return iconCommands;
	}
	
	public static IconCommand matchCommand(String input) {
		
		for (Entry<Pattern, Class<? extends IconCommand>> entry : commandTypesMap.entrySet()) {
			Matcher matcher = entry.getKey().matcher(input);
			if (matcher.find()) {
				// Remove the command prefix and trim the spaces.
				String cleanCommand = matcher.replaceFirst("").trim();
				
				try {
					return entry.getValue().getDeclaredConstructor(String.class).newInstance(cleanCommand);
				} catch (Exception e) {
					// Checked at startup.
				}
			}
		}
		
		return new PlayerIconCommand(input); // Normal command, no match found.
	}
	
}
