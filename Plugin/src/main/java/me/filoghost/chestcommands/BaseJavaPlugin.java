package me.filoghost.chestcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseJavaPlugin extends JavaPlugin {

	@Override
	public final void onEnable() {
		try {
			onCheckedEnable();
		} catch (PluginEnableException e) {
			criticalShutdown(e.getMessage());
		}
	}

	protected abstract void onCheckedEnable() throws PluginEnableException;


	private void criticalShutdown(String errorMessage) {
		String separator = "**********";

		List<String> output = new ArrayList<>();

		output.add(ChatColor.DARK_RED + "[" + getDescription().getName()  + "]" + ChatColor.RED + " Fatal error while enabling plugin:");
		output.add(" ");
		output.add(separator);
		output.add(" ");
		output.add(errorMessage);
		output.add(" ");
		output.add("The plugin has been disabled.");
		output.add(" ");
		output.add(separator);
		output.add(" ");

		Bukkit.getConsoleSender().sendMessage(String.join("\n", output));

		try {
			Thread.sleep(5000);
		} catch (InterruptedException ignored) {}

		Bukkit.getScheduler().runTaskLater(this, () -> {
			Bukkit.getConsoleSender().sendMessage(
					ChatColor.DARK_RED + "[" + getDescription().getName()  + "]"
					+ ChatColor.RED + " Fatal error while enabling plugin. Check previous console logs to find the cause.");
		}, 10);

		setEnabled(false);
	}


	public static class PluginEnableException extends Exception {

		public PluginEnableException(String message) {
			super(message);
		}

	}
}
