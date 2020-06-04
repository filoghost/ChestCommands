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
package me.filoghost.chestcommands.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.filoghost.chestcommands.exception.FormatException;

public class ItemStackReader {

	private Material material = Material.STONE; // In the worst case (bad exception handling) we just get stone
	private int amount = 1;
	private short dataValue = 0;
	private boolean explicitDataValue = false;

	/**
	 * Reads item in the format "id:data, amount"
	 * id can be either the id of the material or its name.
	 * for example wool:5, 3 is a valid input.
	 */
	public ItemStackReader(String input, boolean parseAmount) throws FormatException {
		Validate.notNull(input, "input cannot be null");

		// Remove spaces, they're not needed
		input = StringUtils.stripChars(input, " _-");

		if (parseAmount) {
			// Read the optional amount
			String[] splitAmount = input.split(",");

			if (splitAmount.length > 1) {

				if (!Utils.isValidInteger(splitAmount[1])) {
					throw new FormatException("invalid amount \"" + splitAmount[1] + "\"");
				}

				int amount = Integer.parseInt(splitAmount[1]);
				if (amount <= 0) throw new FormatException("invalid amount \"" + splitAmount[1] + "\"");
				this.amount = amount;

				// Only keep the first part as input
				input = splitAmount[0];
			}
		}


		// Read the optional data value
		String[] splitByColons = input.split(":");

		if (splitByColons.length > 1) {

			if (!Utils.isValidShort(splitByColons[1])) {
				throw new FormatException("invalid data value \"" + splitByColons[1] + "\"");
			}

			short dataValue = Short.parseShort(splitByColons[1]);
			if (dataValue < 0) {
				throw new FormatException("invalid data value \"" + splitByColons[1] + "\"");
			}

			this.explicitDataValue = true;
			this.dataValue = dataValue;

			// Only keep the first part as input
			input = splitByColons[0];
		}

		Material material = MaterialsRegistry.matchMaterial(input);

		if (material == null || MaterialsRegistry.isAir(material)) {
			throw new FormatException("invalid material \"" + input + "\"");
		}
		this.material = material;
	}

	public Material getMaterial() {
		return material;
	}

	public int getAmount() {
		return amount;
	}

	public short getDataValue() {
		return dataValue;
	}

	public boolean hasExplicitDataValue() {
		return explicitDataValue;
	}

	public ItemStack createStack() {
		return new ItemStack(material, amount, dataValue);
	}

}
