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
package me.filoghost.chestcommands.action;

import me.filoghost.chestcommands.hook.VaultEconomyHook;
import me.filoghost.chestcommands.logging.ErrorMessages;
import me.filoghost.chestcommands.parsing.NumberParser;
import me.filoghost.chestcommands.parsing.ParseException;
import org.bukkit.entity.Player;

public class GiveMoneyAction implements Action {

	private final double moneyToGive;

	public GiveMoneyAction(String serializedAction) throws ParseException {
		moneyToGive = NumberParser.getStrictlyPositiveDouble(serializedAction);
	}

	@Override
	public void execute(Player player) {
		if (VaultEconomyHook.INSTANCE.isEnabled()) {
			VaultEconomyHook.giveMoney(player, moneyToGive);
		} else {
			player.sendMessage(ErrorMessages.User.configurationError("Vault with a compatible economy plugin not found"));
		}
	}

}
