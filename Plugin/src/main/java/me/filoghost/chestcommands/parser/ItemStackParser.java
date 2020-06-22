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
package me.filoghost.chestcommands.parser;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.filoghost.chestcommands.util.MaterialsHelper;
import me.filoghost.chestcommands.util.Preconditions;
import me.filoghost.chestcommands.util.Strings;

public class ItemStackParser {

	private final Material material;
	private int amount = 1;
	private short durability = 0;
	private boolean hasExplicitDurability = false;

	/**
	 * Reads item in the format "id:data, amount"
	 * id can be either the id of the material or its name.
	 * for example wool:5, 3 is a valid input.
	 */
	public ItemStackParser(String input, boolean parseAmount) throws ParseException {
		Preconditions.notNull(input, "input");

		if (parseAmount) {
			// Read the optional amount
			String[] splitAmount = Strings.trimmedSplit(input, ",", 2);

			if (splitAmount.length > 1) {
				this.amount = NumberParser.getStrictlyPositiveInteger(splitAmount[1], "invalid amount \"" + splitAmount[1] + "\"");

				// Only keep the first part as input
				input = splitAmount[0];
			}
		}


		// Read the optional durability
		String[] splitByColons = Strings.trimmedSplit(input, ":", 2);

		if (splitByColons.length > 1) {
			short durability = NumberParser.getPositiveShort(splitByColons[1], "invalid durability \"" + splitByColons[1] + "\"");

			this.hasExplicitDurability = true;
			this.durability = durability;

			// Only keep the first part as input
			input = splitByColons[0];
		}

		Material material = MaterialsHelper.matchMaterial(input);

		if (material == null || MaterialsHelper.isAir(material)) {
			throw new ParseException("invalid material \"" + input + "\"");
		}
		this.material = material;
	}

	public Material getMaterial() {
		return material;
	}

	public int getAmount() {
		return amount;
	}

	public short getDurability() {
		return durability;
	}

	public boolean hasExplicitDurability() {
		return hasExplicitDurability;
	}

	public ItemStack createStack() {
		return new ItemStack(material, amount, durability);
	}

}
