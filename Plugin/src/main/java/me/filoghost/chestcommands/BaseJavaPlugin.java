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
		Bukkit.getConsoleSender().sendMessage(getFatalErrorPrefix() + " " + errorMessage);

		Bukkit.getScheduler().runTaskLater(this, () -> {
			Bukkit.getConsoleSender().sendMessage(getPostStartupMessage(errorMessage));
		}, 10);

		setEnabled(false);
	}

	protected String getPostStartupMessage(String errorMessage) {
		List<String> output = new ArrayList<>();

		output.add(getFatalErrorPrefix());
		output.add(" ");
		output.add(errorMessage);
		output.add("The plugin has been disabled.");
		output.add(" ");

		return String.join("\n", output);
	}

	private String getFatalErrorPrefix() {
		return ChatColor.DARK_RED + "[" + getDescription().getName() + "] " + ChatColor.RED + "Fatal error while enabling plugin:";
	}


	public static class PluginEnableException extends Exception {

		public PluginEnableException(String message) {
			super(message);
		}

	}
}
