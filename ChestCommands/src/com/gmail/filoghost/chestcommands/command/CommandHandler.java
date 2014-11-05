package com.gmail.filoghost.chestcommands.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.gmail.filoghost.chestcommands.ChestCommands;
import com.gmail.filoghost.chestcommands.Permissions;
import com.gmail.filoghost.chestcommands.api.IconMenu;
import com.gmail.filoghost.chestcommands.internal.MenuInventoryHolder;
import com.gmail.filoghost.chestcommands.task.ErrorLoggerTask;
import com.gmail.filoghost.chestcommands.util.ErrorLogger;

public class CommandHandler extends CommandFramework {

	public CommandHandler(String label) {
		super(label);
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args) {
		if (args.length == 0) {
			// This info is accessible to anyone. Please don't remove it, remember that Chest Commands is developed for FREE.
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
			
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.getOpenInventory() != null) {
					if (player.getOpenInventory().getTopInventory().getHolder() instanceof MenuInventoryHolder || player.getOpenInventory().getBottomInventory().getHolder() instanceof MenuInventoryHolder) {
						player.closeInventory();
					}
				}
			}
			
			ErrorLogger errorLogger = new ErrorLogger();
			ChestCommands.getInstance().load(errorLogger);
			
			ChestCommands.setLastReloadErrors(errorLogger.getSize());
			
			if (!errorLogger.hasErrors()) {
				sender.sendMessage(ChestCommands.CHAT_PREFIX + "Plugin reloaded.");
			} else {
				new ErrorLoggerTask(errorLogger).run();
				sender.sendMessage(ChestCommands.CHAT_PREFIX + ChatColor.RED + "Plugin reloaded with " + errorLogger.getSize() + " error(s).");
				if (!(sender instanceof ConsoleCommandSender)) {
					sender.sendMessage(ChestCommands.CHAT_PREFIX + ChatColor.RED + "Please check the console.");
				}
			}
			return;
		}
		
		
		
		if (args[0].equalsIgnoreCase("open")) {
			CommandValidate.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "open"), "You don't have permission.");
			CommandValidate.minLength(args, 2, "Usage: /" + label + " open <menu> [player]");
			
			Player target = null;
			
			if (!(sender instanceof Player)) {
				CommandValidate.minLength(args, 3, "You must specify a player from the console.");
				target = Bukkit.getPlayerExact(args[2]);
			} else {
				if (args.length > 2) {
					CommandValidate.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "open.others"), "You don't have permission.");
					target = Bukkit.getPlayerExact(args[2]);
				} else {
					target = (Player) sender;
				}
				
			}
			
			CommandValidate.notNull(target, "That player is not online.");
			
			IconMenu menu = ChestCommands.getFileNameToMenuMap().get(args[1].toLowerCase().endsWith(".yml") ? args[1] : args[1] + ".yml");
			CommandValidate.notNull(target, "That menu was not found.");

			if (sender.getName().equalsIgnoreCase(target.getName())) {
				sender.sendMessage(ChatColor.GREEN + "Opening the menu \"" + args[1] + "\".");
			} else {
				sender.sendMessage(ChatColor.GREEN + "Opening the menu \"" + args[1] + "\" to " + target.getName() + ".");
			}
			
			menu.open(target);
			return;
		}
		
		
		
		if (args[0].equalsIgnoreCase("list")) {
			CommandValidate.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "list"), "You don't have permission.");
			sender.sendMessage(ChestCommands.CHAT_PREFIX + " Loaded menus:");
			for (String file : ChestCommands.getFileNameToMenuMap().keySet()) {
				sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.WHITE + file);
			}
			
			return;
		}
		
		sender.sendMessage(ChatColor.RED + "Unknown sub-command \"" + args[0] + "\".");
	}

}
