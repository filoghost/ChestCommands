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
package me.filoghost.chestcommands.parsing.attribute;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;

public class ViewPermissionAttribute implements ApplicableIconAttribute {

	private final String viewPermission;

	public ViewPermissionAttribute(String viewPermission, AttributeErrorHandler errorHandler) {
		this.viewPermission = viewPermission;
	}
	
	@Override
	public void apply(InternalConfigurableIcon icon) {
		icon.setViewPermission(viewPermission);
	}

}