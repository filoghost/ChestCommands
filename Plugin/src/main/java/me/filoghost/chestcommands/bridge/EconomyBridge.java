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

import me.filoghost.chestcommands.util.Preconditions;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class EconomyBridge {

	private static Economy economy;

	public static boolean setupEconomy() {
		if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> economyServiceProvider = Bukkit.getServicesManager().getRegistration(Economy.class);
		if (economyServiceProvider == null) {
			return false;
		}
		economy = economyServiceProvider.getProvider();
		return economy != null;
	}

	public static boolean hasValidEconomy() {
		return economy != null;
	}

	public static double getMoney(Player player) {
		checkValidEconomy();
		return economy.getBalance(player, player.getWorld().getName());
	}

	public static boolean hasMoney(Player player, double minimum) {
		checkValidEconomy();
		checkPositiveAmount(minimum);

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
		checkValidEconomy();
		checkPositiveAmount(amount);

		EconomyResponse response = economy.withdrawPlayer(player, player.getWorld().getName(), amount);
		boolean result = response.transactionSuccess();

		return result;
	}

	public static boolean giveMoney(Player player, double amount) {
		checkValidEconomy();
		checkPositiveAmount(amount);

		EconomyResponse response = economy.depositPlayer(player, player.getWorld().getName(), amount);
		boolean result = response.transactionSuccess();

		return result;
	}

	private static void checkValidEconomy() {
		Preconditions.checkState(hasValidEconomy(), "economy plugin not found");
	}
	
	private static void checkPositiveAmount(double amount) {
		Preconditions.checkArgument(amount >= 0.0, "amount cannot be negative");
	}

	public static String formatMoney(double amount) {
		if (hasValidEconomy()) {
			return economy.format(amount);
		} else {
			return Double.toString(amount);
		}
	}
}
