package com.gmail.filoghost.chestcommands.config;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringEscapeUtils;

import com.gmail.filoghost.chestcommands.ChestCommands;
import com.gmail.filoghost.chestcommands.util.ErrorLogger;
import com.gmail.filoghost.chestcommands.util.Utils;

/**
 *  This is not a real YAML file ;)
 */
public class AsciiPlaceholders {

	private static Map<String, String> placeholders = Utils.newHashMap();
	
	
	public static void load(ErrorLogger errorLogger) throws IOException, Exception {
		
		placeholders.clear();
		File file = new File(ChestCommands.getInstance().getDataFolder(), "placeholders.yml");
		
		if (!file.exists()) {
			Utils.saveResourceSafe(ChestCommands.getInstance(), "placeholders.yml");
		}
		
		List<String> lines = Utils.readLines(file);
		for (String line : lines) {
			
			// Comment or empty line.
			if (line.isEmpty() || line.startsWith("#"))  {
				continue;
			}
			
			if (!line.contains(":")) {
				errorLogger.addError("Unable to parse a line(" + line + ") from placeholders.yml: it must contain ':' to separate the placeholder and the replacement.");
				continue;
			}
				
			int indexOf = line.indexOf(':');
			String placeholder = unquote(line.substring(0, indexOf).trim());
			String replacement = Utils.addColors(StringEscapeUtils.unescapeJava(unquote(line.substring(indexOf + 1, line.length()).trim())));

			if (placeholder.length() == 0 || replacement.length() == 0) {
				errorLogger.addError("Unable to parse a line(" + line + ") from placeholders.yml: the placeholder and the replacement must have both at least 1 character.");
				continue;
			}
			
			if (placeholder.length() > 100) {
				errorLogger.addError("Unable to parse a line(" + line + ") from placeholders.yml: the placeholder cannot be longer than 100 characters.");
				continue;
			}
			
			placeholders.put(placeholder, replacement);
		}
	}
	
	public static List<String> placeholdersToSymbols(List<String> input) {
		if (input == null) return null;
		for (int i = 0; i < input.size(); i++) {
			input.set(i, placeholdersToSymbols(input.get(i)));
		}
		return input;
	}
	
	public static String placeholdersToSymbols(String input) {
		if (input == null) return null;
		for (Entry<String, String> entry : placeholders.entrySet()) {
			input = input.replace(entry.getKey(), entry.getValue());
		}
		return input;
	}
	
	public static String symbolsToPlaceholders(String input) {
		if (input == null) return null;
		for (Entry<String, String> entry : placeholders.entrySet()) {
			input = input.replace(entry.getValue(), entry.getKey());
		}
		return input;
	}
	
	private static String unquote(String input) {
		if (input.length() < 2) {
			// Cannot be quoted.
			return input;
		}
		if (input.startsWith("'") && input.endsWith("'")) {
			return input.substring(1, input.length() - 1);
		} else if (input.startsWith("\"") && input.endsWith("\"")) {
			return input.substring(1, input.length() - 1);
		}
		
		return input;
	}
}
