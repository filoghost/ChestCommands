package com.gmail.filoghost.chestcommands.serializer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gmail.filoghost.chestcommands.internal.icon.IconCommand;
import com.gmail.filoghost.chestcommands.internal.icon.command.BroadcastCommand;
import com.gmail.filoghost.chestcommands.internal.icon.command.ConsoleCommand;
import com.gmail.filoghost.chestcommands.internal.icon.command.GiveCommand;
import com.gmail.filoghost.chestcommands.internal.icon.command.GiveMoneyCommand;
import com.gmail.filoghost.chestcommands.internal.icon.command.OpCommand;
import com.gmail.filoghost.chestcommands.internal.icon.command.OpenCommand;
import com.gmail.filoghost.chestcommands.internal.icon.command.PlayerCommand;
import com.gmail.filoghost.chestcommands.internal.icon.command.ServerCommand;
import com.gmail.filoghost.chestcommands.internal.icon.command.SoundCommand;
import com.gmail.filoghost.chestcommands.internal.icon.command.TellCommand;
import com.gmail.filoghost.chestcommands.util.ErrorLogger;
import com.google.common.collect.Lists;

public class CommandSerializer {

	private static Map<Pattern, Class<? extends IconCommand>> commandTypesMap = new HashMap<Pattern, Class<? extends IconCommand>>();
	static {
		commandTypesMap.put(commandPattern("console:"), ConsoleCommand.class);
		commandTypesMap.put(commandPattern("op:"), OpCommand.class);
		commandTypesMap.put(commandPattern("open:"), OpenCommand.class);
		commandTypesMap.put(commandPattern("server:?"), ServerCommand.class); // The colon is optional.
		commandTypesMap.put(commandPattern("tell:"), TellCommand.class);
		commandTypesMap.put(commandPattern("broadcast:"), BroadcastCommand.class);
		commandTypesMap.put(commandPattern("give:"), GiveCommand.class);
		commandTypesMap.put(commandPattern("give-?money:"), GiveMoneyCommand.class);
		commandTypesMap.put(commandPattern("sound:"), SoundCommand.class);
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
		
		if (input.contains(";")) {
			
			String[] split = input.split(";");
			List<IconCommand> iconCommands = Lists.newArrayList();
			
			for (String command : split) {
				String trim = command.trim();
				
				if (trim.length() > 0) {
					iconCommands.add(matchCommand(trim));
				}
			}
			
			return iconCommands;
			
		} else {
			
			return Arrays.asList(matchCommand(input));
		}
	}
	
	public static IconCommand matchCommand(String input) {
		
		for (Entry<Pattern, Class<? extends IconCommand>> entry : commandTypesMap.entrySet()) {
			Matcher matcher = entry.getKey().matcher(input);
			if (matcher.find()) {
				
				String cleanedCommand = matcher.replaceFirst("");
				
				try {
					return entry.getValue().getDeclaredConstructor(String.class).newInstance(cleanedCommand);
				} catch (Exception e) {
					// Checked at startup.
				}
			}
		}
		
		return new PlayerCommand(input); // Normal command, no match found.
	}
	
}
