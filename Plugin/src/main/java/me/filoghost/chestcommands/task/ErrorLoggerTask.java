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
package me.filoghost.chestcommands.task;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.util.ErrorLogger;
import me.filoghost.chestcommands.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

public class ErrorLoggerTask implements Runnable {

	private ErrorLogger errorLogger;

	public ErrorLoggerTask(ErrorLogger errorLogger) {
		this.errorLogger = errorLogger;
	}

	@Override
	public void run() {

		List<String> lines = new ArrayList<>();

		lines.add(" ");
		lines.add(ChatColor.RED + "#------------------- Chest Commands Errors -------------------#");
		int count = 1;
		for (String error : errorLogger.getErrors()) {
			lines.add(ChatColor.GRAY + "" + (count++) + ") " + ChatColor.WHITE + error);
		}
		lines.add(ChatColor.RED + "#-------------------------------------------------------------#");

		String output = StringUtils.join(lines, "\n");

		if (ChestCommands.getSettings().use_console_colors) {
			Bukkit.getConsoleSender().sendMessage(output);
		} else {
			System.out.println(ChatColor.stripColor(output));
		}
	}

}
