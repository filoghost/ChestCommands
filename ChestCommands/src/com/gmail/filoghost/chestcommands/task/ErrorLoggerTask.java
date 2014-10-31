package com.gmail.filoghost.chestcommands.task;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.gmail.filoghost.chestcommands.ChestCommands;
import com.gmail.filoghost.chestcommands.util.ErrorLogger;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class ErrorLoggerTask implements Runnable {

	private ErrorLogger errorLogger;

	public ErrorLoggerTask(ErrorLogger errorLogger) {
		this.errorLogger = errorLogger;
	}

	@Override
	public void run() {
		
		List<String> lines = Lists.newArrayList();
		
		lines.add(" ");
		lines.add(ChatColor.RED + "#------------------- Chest Commands Errors -------------------#");
		int count = 1;
		for (String error : errorLogger.getErrors()) {
			lines.add(ChatColor.GRAY + "" + (count++) + ") " + ChatColor.WHITE + error);
		}
		lines.add(ChatColor.RED + "#-------------------------------------------------------------#");
		
		String output = Joiner.on('\n').join(lines);
		
		if (ChestCommands.getSettings().use_console_colors) {
			Bukkit.getConsoleSender().sendMessage(output);
		} else {
			System.out.println(ChatColor.stripColor(output));
		}
	}
	
}
