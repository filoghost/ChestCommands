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
package me.filoghost.chestcommands.parsing.icon;

import me.filoghost.chestcommands.logging.ErrorMessages;
import me.filoghost.chestcommands.parsing.ParseException;
import me.filoghost.chestcommands.util.logging.ErrorCollector;

public class AttributeErrorCollector {

	private final ErrorCollector errorCollector;
	private final IconSettings iconSettings;
	private final String attributeName;

	public AttributeErrorCollector(ErrorCollector errorCollector, IconSettings iconSettings, String attributeName) {
		this.errorCollector = errorCollector;
		this.iconSettings = iconSettings;
		this.attributeName = attributeName;
	}

	public void addListElementError(String listElement, ParseException e) {
		errorCollector.add(ErrorMessages.Menu.invalidAttributeListElement(iconSettings, attributeName, listElement), e);
	}

}
