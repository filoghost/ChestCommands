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
package me.filoghost.chestcommands.logging;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.config.framework.exception.ConfigException;
import me.filoghost.chestcommands.config.framework.exception.ConfigSyntaxException;
import me.filoghost.chestcommands.legacy.UpgradeExecutorException;
import me.filoghost.chestcommands.legacy.upgrade.UpgradeException;
import me.filoghost.chestcommands.parsing.ParseException;
import me.filoghost.chestcommands.util.logging.ErrorCollector;
import me.filoghost.chestcommands.util.logging.ErrorInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class PrintableErrorCollector extends ErrorCollector {


	@Override
	public void logToConsole() {
		StringBuilder output = new StringBuilder();

		if (errors.size() > 0) {
			output.append(ChestCommands.CHAT_PREFIX).append(ChatColor.RED).append("Encountered ").append(errors.size()).append(" error(s) on load:\n");
			output.append(" \n");

			int index = 1;
			for (ErrorInfo error : errors) {
				ErrorPrintInfo printFormat = getErrorPrintInfo(index, error);
				printError(output, printFormat);
				index++;
			}
		}

		Bukkit.getConsoleSender().sendMessage(output.toString());
	}

	private ErrorPrintInfo getErrorPrintInfo(int index, ErrorInfo error) {
		List<String> message = error.getMessage();
		String details = null;
		Throwable cause = error.getCause();

		// Recursively inspect the cause until an unknown or null exception is found
		while (true) {
			if (cause instanceof ConfigSyntaxException) {
				message.add(cause.getMessage());
				details = ((ConfigSyntaxException) cause).getParsingErrorDetails();
				cause = null; // Do not print stacktrace for syntax exceptions

			} else if (cause instanceof ConfigException
					|| cause instanceof ParseException
					|| cause instanceof UpgradeException
					|| cause instanceof UpgradeExecutorException) {
				message.add(cause.getMessage());
				cause = cause.getCause(); // Print the cause (or nothing if null), not our "known" exception

			} else {
				return new ErrorPrintInfo(index, message, details, cause);
			}
		}
	}

	private void printError(StringBuilder output, ErrorPrintInfo error) {
		output.append(ChatColor.YELLOW).append(error.getIndex()).append(") ");
		output.append(ChatColor.WHITE).append(MessagePartJoiner.join(error.getMessage()));

		if (error.getDetails() != null) {
			output.append(". Details:\n");
			output.append(ChatColor.YELLOW).append(error.getDetails()).append("\n");
		} else {
			output.append(".\n");
		}
		if (error.getCause() != null) {
			output.append(ChatColor.DARK_GRAY);
			output.append("--------[ Exception details ]--------\n");
			output.append(getStackTraceString(error.getCause()));
			output.append("-------------------------------------\n");
		}
		output.append(" \n");
		output.append(ChatColor.RESET);
	}

	private String getStackTraceString(Throwable throwable) {
		StringWriter stringWriter = new StringWriter();
		throwable.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}

}
