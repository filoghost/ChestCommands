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
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.logging.ErrorMessages;
import me.filoghost.chestcommands.parsing.ParseException;

public class AmountAttribute implements IconAttribute {

	private final int amount;

	public AmountAttribute(int amount, AttributeErrorHandler errorHandler) throws ParseException {
		if (amount < 0) {
			throw new ParseException(ErrorMessages.Parsing.zeroOrPositive);
		}
		this.amount = amount;
	}
	
	@Override
	public void apply(InternalConfigurableIcon icon) {
		icon.setAmount(amount);
	}

}
