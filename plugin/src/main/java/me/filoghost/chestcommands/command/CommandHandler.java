/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.command;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.Permissions;
import me.filoghost.chestcommands.menu.InternalMenu;
import me.filoghost.chestcommands.menu.MenuManager;
import me.filoghost.chestcommands.util.Utils;
import me.filoghost.fcommons.collection.CaseInsensitiveString;
import me.filoghost.fcommons.command.CommandContext;
import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.sub.annotated.AnnotatedSubCommand;
import me.filoghost.fcommons.command.sub.annotated.AnnotatedSubCommandManager;
import me.filoghost.fcommons.command.sub.annotated.Description;
import me.filoghost.fcommons.command.sub.annotated.DisplayPriority;
import me.filoghost.fcommons.command.sub.annotated.MinArgs;
import me.filoghost.fcommons.command.sub.annotated.Name;
import me.filoghost.fcommons.command.sub.annotated.Permission;
import me.filoghost.fcommons.command.sub.annotated.UsageArgs;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.fcommons.command.validation.CommandValidate;
import me.filoghost.fcommons.logging.ErrorCollector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandHandler extends AnnotatedSubCommandManager {

    public CommandHandler(String label) {
        setName(label);
    }

    @Override
    protected String getDefaultSubCommandPermission(AnnotatedSubCommand subCommand) {
        return Permissions.COMMAND_PREFIX + "." + subCommand.getName();
    }

    @Override
    protected void sendNoArgsMessage(CommandContext context) {
        CommandSender sender = context.getSender();
        sender.sendMessage(ChestCommands.CHAT_PREFIX);
        sender.sendMessage(ChatColor.GREEN + "Version: " + ChatColor.GRAY + ChestCommands.getInstance().getDescription().getVersion());
        sender.sendMessage(ChatColor.GREEN + "Developer: " + ChatColor.GRAY + "filoghost");
        sender.sendMessage(ChatColor.GREEN + "Commands: " + ChatColor.GRAY + "/" + context.getRootLabel() + " help");
    }
    
    @Override
    protected void sendUnknownSubCommandMessage(SubCommandContext context) {
        context.getSender().sendMessage(ChatColor.RED + "Unknown sub-command \"" + context.getSubLabel() + "\". "
                + "Use \"/" + context.getRootLabel() + " help\" to see available commands.");
    }

    @Name("help")
    @Permission(Permissions.COMMAND_PREFIX + "help")
    public void help(CommandSender sender, SubCommandContext context) {
        sender.sendMessage(ChestCommands.CHAT_PREFIX + "Commands:");
        for (AnnotatedSubCommand subCommand : getSubCommands()) {
            if (subCommand == context.getSubCommand()) {
                continue;
            }
            String usageText = getUsageText(context, subCommand);
            sender.sendMessage(ChatColor.WHITE + usageText + ChatColor.GRAY + " - " + subCommand.getDescription());
        }
    }

    @Name("reload")
    @Description("Reloads the plugin.")
    @Permission(Permissions.COMMAND_PREFIX + "reload")
    @DisplayPriority(100)
    public void reload(CommandSender sender) {
        MenuManager.closeAllOpenMenuViews();

        ErrorCollector errorCollector = ChestCommands.load();

        if (!errorCollector.hasErrors()) {
            sender.sendMessage(ChestCommands.CHAT_PREFIX + "Plugin reloaded.");
        } else {
            errorCollector.logToConsole();
            sender.sendMessage(ChestCommands.CHAT_PREFIX + ChatColor.RED + "Plugin reloaded with " + errorCollector.getErrorsCount() + " error(s).");
            if (!(sender instanceof ConsoleCommandSender)) {
                sender.sendMessage(ChestCommands.CHAT_PREFIX + ChatColor.RED + "Please check the console.");
            }
        }
    }

    @Name("errors")
    @Description("Displays the last load errors on the console.")
    @Permission(Permissions.COMMAND_PREFIX + "errors")
    @DisplayPriority(3)
    public void errors(CommandSender sender) {
        ErrorCollector errorCollector = ChestCommands.getLastLoadErrors();

        if (errorCollector.hasErrors()) {
            errorCollector.logToConsole();
            sender.sendMessage(ChestCommands.CHAT_PREFIX + ChatColor.RED + "Last time the plugin loaded, "
                    + errorCollector.getErrorsCount() + " error(s) were found.");
            if (!(sender instanceof ConsoleCommandSender)) {
                sender.sendMessage(ChestCommands.CHAT_PREFIX + ChatColor.RED + "Errors were printed on the console.");
            }
        } else {
            sender.sendMessage(ChestCommands.CHAT_PREFIX + ChatColor.GREEN + "Last plugin load was successful, no errors logged.");
        }
    }

    @Name("list")
    @Description("Lists the loaded menus.")
    @Permission(Permissions.COMMAND_PREFIX + "list")
    @DisplayPriority(2)
    public void list(CommandSender sender) {
        sender.sendMessage(ChestCommands.CHAT_PREFIX + "Loaded menus:");
        for (CaseInsensitiveString name : MenuManager.getMenuFileNames()) {
            sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.WHITE + name);
        }
    }

    @Name("open")
    @Description("Opens a menu for a player.")
    @Permission(Permissions.COMMAND_PREFIX + "open")
    @MinArgs(1)
    @UsageArgs("<menu> [player]")
    @DisplayPriority(1)
    @SuppressWarnings("deprecation")
    public void open(CommandSender sender, String[] args) throws CommandException {
        Player target;

        if (sender instanceof Player) {
            if (args.length > 1) {
                CommandValidate.check(sender.hasPermission(Permissions.COMMAND_PREFIX + "open.others"),
                        "You don't have the permission to open a menu for other players.");
                target = Bukkit.getPlayerExact(args[1]);
            } else {
                target = (Player) sender;
            }
        } else {
            CommandValidate.minLength(args, 2, "You must specify a player from the console.");
            target = Bukkit.getPlayerExact(args[1]);
        }

        CommandValidate.notNull(target, "That player is not online.");

        String menuName = Utils.addYamlExtension(args[0]);
        InternalMenu menu = MenuManager.getMenuByFileName(menuName);
        CommandValidate.notNull(menu, "The menu \"" + menuName + "\" was not found.");

        if (!sender.hasPermission(menu.getOpenPermission())) {
            menu.sendNoOpenPermissionMessage(sender);
            return;
        }

        if (sender.getName().equalsIgnoreCase(target.getName())) {
            sender.sendMessage(ChatColor.GREEN + "Opening the menu " + menuName + ".");
        } else {
            sender.sendMessage(ChatColor.GREEN + "Opening the menu " + menuName + " to " + target.getName() + ".");
        }

        menu.open(target);
    }

}
