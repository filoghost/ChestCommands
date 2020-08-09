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

public class ExpLevelsAttribute implements IconAttribute {

	private final int expLevels;

	public ExpLevelsAttribute(int expLevels, AttributeErrorHandler errorHandler) throws ParseException {
		if (expLevels < 0) {
			throw new ParseException(ErrorMessages.Parsing.zeroOrPositive);
		}
		this.expLevels = expLevels;
	}
	
	@Override
	public void apply(InternalConfigurableIcon icon) {
		icon.setRequiredExpLevel(expLevels);
	}

}
