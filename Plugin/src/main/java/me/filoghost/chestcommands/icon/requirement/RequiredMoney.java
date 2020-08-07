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
package me.filoghost.chestcommands.icon.requirement;

import com.google.common.base.Preconditions;
import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.hook.VaultEconomyHook;
import me.filoghost.chestcommands.logging.ErrorMessages;
import org.bukkit.entity.Player;

public class RequiredMoney implements Requirement {

	private final double moneyAmount;
	
	public RequiredMoney(double moneyAmount) {
		Preconditions.checkArgument(moneyAmount > 0.0, "money amount must be positive");
		this.moneyAmount = moneyAmount;
	}

	@Override
	public boolean hasCost(Player player) {
		if (!VaultEconomyHook.INSTANCE.isEnabled()) {
			player.sendMessage(ErrorMessages.User.configurationError(
					"the item has a price, but Vault with a compatible economy plugin was not found. "
					+ "For security, the action has been blocked"));
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
			player.sendMessage(ErrorMessages.User.configurationError("a money transaction couldn't be executed"));
		}
		
		return success;
	}	

}
