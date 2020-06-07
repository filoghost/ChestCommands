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
package me.filoghost.chestcommands.bridge;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import me.filoghost.chestcommands.MenuManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class EconomyBridge {

	private static Economy economy;

	public static boolean setupEconomy() {
		if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		economy = rsp.getProvider();
		return economy != null;
	}

	public static boolean hasValidEconomy() {
		return economy != null;
	}

	public static double getMoney(Player player) {
		if (!hasValidEconomy()) throw new IllegalStateException("Economy plugin was not found!");
		return economy.getBalance(player, player.getWorld().getName());
	}

	public static boolean hasMoney(Player player, double minimum) {
		if (!hasValidEconomy()) throw new IllegalStateException("Economy plugin was not found!");
		if (minimum < 0.0) throw new IllegalArgumentException("Invalid amount of money: " + minimum);

		double balance = economy.getBalance(player, player.getWorld().getName());

		if (balance < minimum) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @return true if the operation was successful.
	 */
	public static boolean takeMoney(Player player, double amount) {
		if (!hasValidEconomy()) throw new IllegalStateException("Economy plugin was not found!");
		if (amount < 0.0) throw new IllegalArgumentException("Invalid amount of money: " + amount);

		EconomyResponse response = economy.withdrawPlayer(player, player.getWorld().getName(), amount);
		boolean result = response.transactionSuccess();

		MenuManager.refreshOpenMenu(player);

		return result;
	}

	public static boolean giveMoney(Player player, double amount) {
		if (!hasValidEconomy()) throw new IllegalStateException("Economy plugin was not found!");
		if (amount < 0.0) throw new IllegalArgumentException("Invalid amount of money: " + amount);

		EconomyResponse response = economy.depositPlayer(player, player.getWorld().getName(), amount);
		boolean result = response.transactionSuccess();

		MenuManager.refreshOpenMenu(player);

		return result;
	}

	public static String formatMoney(double amount) {
		if (hasValidEconomy()) {
			return economy.format(amount);
		} else {
			return Double.toString(amount);
		}
	}
}
