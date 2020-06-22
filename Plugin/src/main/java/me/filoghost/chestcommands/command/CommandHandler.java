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
package me.filoghost.chestcommands.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.Permissions;
import me.filoghost.chestcommands.command.framework.CommandFramework;
import me.filoghost.chestcommands.command.framework.CommandValidate;
import me.filoghost.chestcommands.menu.AdvancedIconMenu;
import me.filoghost.chestcommands.menu.MenuManager;
import me.filoghost.chestcommands.util.ErrorCollector;

public class CommandHandler extends CommandFramework {
	
	private MenuManager menuManager;
	
	public CommandHandler(MenuManager menuManager, String label) {
		super(label);
		this.menuManager = menuManager;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String label, String[] args) {
		if (args.length == 0) {
			// This info is accessible to anyone. Please don't remove it, remember that Chest Commands is developed for free
			sender.sendMessage(ChestCommands.CHAT_PREFIX);
			sender.sendMessage(ChatColor.GREEN + "Version: " + ChatColor.GRAY + ChestCommands.getInstance().getDescription().getVersion());
			sender.sendMessage(ChatColor.GREEN + "Developer: " + ChatColor.GRAY + "filoghost");
			sender.sendMessage(ChatColor.GREEN + "Commands: " + ChatColor.GRAY + "/" + label + " help");
			return;
		}


		if (args[0].equalsIgnoreCase("help")) {
			CommandValidate.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "help"), "You don't have permission.");
			sender.sendMessage(ChestCommands.CHAT_PREFIX + " Commands:");
			sender.sendMessage(ChatColor.WHITE + "/" + label + " reload" + ChatColor.GRAY + " - Reloads the plugin.");
			sender.sendMessage(ChatColor.WHITE + "/" + label + " list" + ChatColor.GRAY + " - Lists the loaded menus.");
			sender.sendMessage(ChatColor.WHITE + "/" + label + " open <menu> [player]" + ChatColor.GRAY + " - Opens a menu for a player.");
			return;
		}


		if (args[0].equalsIgnoreCase("reload")) {
			CommandValidate.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "reload"), "You don't have permission.");

			ChestCommands.closeAllMenus();

			ErrorCollector errors = ChestCommands.getInstance().load();

			if (!errors.hasWarningsOrErrors()) {
				sender.sendMessage(ChestCommands.CHAT_PREFIX + "Plugin reloaded.");
			} else {
				errors.logToConsole();
				sender.sendMessage(ChestCommands.CHAT_PREFIX + ChatColor.RED + "Plugin reloaded with " + errors.getWarningsCount() + " warning(s) and " + errors.getErrorsCount() + " error(s).");
				if (!(sender instanceof ConsoleCommandSender)) {
					sender.sendMessage(ChestCommands.CHAT_PREFIX + ChatColor.RED + "Please check the console.");
				}
			}
			return;
		}


		if (args[0].equalsIgnoreCase("open")) {
			CommandValidate.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "open"), "You don't have permission.");
			CommandValidate.minLength(args, 2, "Usage: /" + label + " open <menu> [player]");

			Player target;

			if (sender instanceof Player) {
				if (args.length > 2) {
					CommandValidate.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "open.others"), "You don't have permission to open menus for others.");
					target = Bukkit.getPlayerExact(args[2]);
				} else {
					target = (Player) sender;
				}
			} else {
				CommandValidate.minLength(args, 3, "You must specify a player from the console.");
				target = Bukkit.getPlayerExact(args[2]);
			}

			CommandValidate.notNull(target, "That player is not online.");

			String menuName = args[1].toLowerCase().endsWith(".yml") ? args[1] : args[1] + ".yml";
			AdvancedIconMenu menu = menuManager.getMenuByFileName(menuName);
			CommandValidate.notNull(menu, "The menu \"" + menuName + "\" was not found.");

			if (!sender.hasPermission(menu.getOpenPermission())) {
				menu.sendNoOpenPermissionMessage(sender);
				return;
			}

			if (sender.getName().equalsIgnoreCase(target.getName())) {
				if (!ChestCommands.getLang().open_menu.isEmpty()) {
					sender.sendMessage(ChestCommands.getLang().open_menu.replace("{menu}", menuName));
				}
			} else {
				if (!ChestCommands.getLang().open_menu_others.isEmpty()) {
					sender.sendMessage(ChestCommands.getLang().open_menu_others.replace("{menu}", menuName).replace("{player}", target.getName()));
				}
			}

			menu.open(target);
			return;
		}


		if (args[0].equalsIgnoreCase("list")) {
			CommandValidate.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "list"), "You don't have permission.");
			sender.sendMessage(ChestCommands.CHAT_PREFIX + " Loaded menus:");
			for (String file : menuManager.getMenuFileNames()) {
				sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.WHITE + file);
			}

			return;
		}

		sender.sendMessage(ChatColor.RED + "Unknown sub-command \"" + args[0] + "\".");
	}

}
