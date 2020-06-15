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
package me.filoghost.chestcommands.hook;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import me.filoghost.chestcommands.util.Preconditions;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public enum VaultEconomyHook implements PluginHook {

	INSTANCE;
	
	private Economy economy;

	@Override
	public void setup() {
		economy = null;
		
		if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
			return;
		}
		
		RegisteredServiceProvider<Economy> economyServiceProvider = Bukkit.getServicesManager().getRegistration(Economy.class);
		if (economyServiceProvider == null) {
			return;
		}
		
		economy = economyServiceProvider.getProvider();
	}

	@Override
	public boolean isEnabled() {
		return economy != null;
	}

	public static double getMoney(Player player) {
		INSTANCE.checkEnabledState();
		return INSTANCE.economy.getBalance(player, player.getWorld().getName());
	}

	public static boolean hasMoney(Player player, double minimum) {
		INSTANCE.checkEnabledState();
		checkPositiveAmount(minimum);

		double balance = INSTANCE.economy.getBalance(player, player.getWorld().getName());

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
		INSTANCE.checkEnabledState();
		checkPositiveAmount(amount);

		EconomyResponse response = INSTANCE.economy.withdrawPlayer(player, player.getWorld().getName(), amount);
		boolean result = response.transactionSuccess();

		return result;
	}

	public static boolean giveMoney(Player player, double amount) {
		INSTANCE.checkEnabledState();
		checkPositiveAmount(amount);

		EconomyResponse response = INSTANCE.economy.depositPlayer(player, player.getWorld().getName(), amount);
		boolean result = response.transactionSuccess();

		return result;
	}
	
	private static void checkPositiveAmount(double amount) {
		Preconditions.checkArgument(amount >= 0.0, "amount cannot be negative");
	}

	public static String formatMoney(double amount) {
		if (INSTANCE.economy != null) {
			return INSTANCE.economy.format(amount);
		} else {
			return Double.toString(amount);
		}
	}
}
