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
package me.filoghost.chestcommands.command.framework;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Wrapper for the default command executor.
 */
public abstract class CommandFramework implements CommandExecutor {

	private final String label;


	public CommandFramework(String label) {
		this.label = label;
	}


	public abstract void execute(CommandSender sender, String label, String[] args);


	/**
	 * Default implementation of Bukkit's command executor.
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		try {
			execute(sender, label, args);

		} catch (CommandException ex) {
			if (ex.getMessage() != null && !ex.getMessage().isEmpty()) {
				// Use RED by default
				sender.sendMessage(ChatColor.RED + ex.getMessage());
			}
		}

		return true;
	}


	/**
	 * Register a command through the framework.
	 */
	public static boolean register(JavaPlugin plugin, CommandFramework command) {
		PluginCommand pluginCommand = plugin.getCommand(command.label);

		if (pluginCommand == null) {
			return false;
		}

		pluginCommand.setExecutor(command);
		return true;
	}

}
