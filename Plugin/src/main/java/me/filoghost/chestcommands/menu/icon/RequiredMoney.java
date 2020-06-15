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
package me.filoghost.chestcommands.menu.icon;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.common.base.Preconditions;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.hook.VaultEconomyHook;

public class RequiredMoney implements Requirement {

	private final double moneyAmount;
	
	public RequiredMoney(double moneyAmount) {
		Preconditions.checkArgument(moneyAmount > 0.0, "money amount must be positive");
		this.moneyAmount = moneyAmount;
	}
	
	public double getAmount() {
		return moneyAmount;
	}

	@Override
	public boolean check(Player player) {
		if (!VaultEconomyHook.INSTANCE.isEnabled()) {
			player.sendMessage(ChatColor.RED + "This action has a price, but Vault with a compatible economy plugin was not found. For security, the action has been blocked. Please inform the staff.");
			return false;
		}

		if (!VaultEconomyHook.hasMoney(player, moneyAmount)) {
			player.sendMessage(ChestCommands.getLang().no_money.replace("{money}", VaultEconomyHook.formatMoney(moneyAmount)));
			return false;
		}
		
		return true;
	}

	@Override
	public boolean takeCost(Player player) {
		boolean success = VaultEconomyHook.takeMoney(player, moneyAmount);
		
		if (!success) {
			player.sendMessage(ChatColor.RED + "Error: a money transaction couldn't be executed. Please inform the staff.");
		}
		
		return success;
	}	

}
