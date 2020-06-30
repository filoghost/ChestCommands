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
package me.filoghost.chestcommands.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * This is a class to collect all the errors found while loading the plugin.
 */
public class ErrorCollector {

	private final List<String> errors = new ArrayList<>();
	private final List<String> warnings = new ArrayList<>();
	
	public void addError(String error) {
		errors.add(error);
	}
	
	public void addWarning(String warning) {
		warnings.add(warning);
	}
	
	public int getErrorsCount() {
		return errors.size();
	}
	
	public int getWarningsCount() {
		return warnings.size();
	}
	
	public boolean hasWarningsOrErrors() {
		return errors.size() > 0 || warnings.size() > 0;
	}
	
	public void logToConsole() {
		StringBuilder msg = new StringBuilder(200);
		msg.append(" \n \n");
		
		if (errors.size() > 0) {
			msg.append(ChatColor.RED + "[ChestCommands] Encountered " + errors.size() + " error(s) on load:\n");
			appendNumberedList(msg, errors);
		}
		
		if (warnings.size() > 0) {
			msg.append(ChatColor.YELLOW + "[ChestCommands] Encountered " + warnings.size() + " warnings(s) on load:\n");
			appendNumberedList(msg, warnings);
		}

		Bukkit.getConsoleSender().sendMessage(msg.toString());
	}
	
	private void appendNumberedList(StringBuilder output, List<String> lines) {
		int count = 1;
		for (String line : lines) {
			output.append(ChatColor.WHITE).append(count).append(") ").append(line);
			output.append("\n");
			count++;
		}
	}
	
}
