/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.parsing;

import me.filoghost.chestcommands.logging.ErrorMessages;
import me.filoghost.commons.MaterialsHelper;
import me.filoghost.commons.Preconditions;
import me.filoghost.commons.Strings;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class ItemStackParser {

	private final Material material;
	private int amount = 1;
	private short durability = 0;
	private boolean hasExplicitDurability = false;

	/**
	 * Reads item in the format "material:durability, amount".
	 */
	public ItemStackParser(String input, boolean parseAmount) throws ParseException {
		Preconditions.notNull(input, "input");

		if (parseAmount) {
			// Read the optional amount
			String[] splitAmount = Strings.trimmedSplit(input, ",", 2);

			if (splitAmount.length > 1) {
				try {
					this.amount = NumberParser.getStrictlyPositiveInteger(splitAmount[1]);
				} catch (ParseException e) {
					throw new ParseException(ErrorMessages.Parsing.invalidAmount(splitAmount[1]), e);
				}

				// Only keep the first part as input
				input = splitAmount[0];
			}
		}


		// Read the optional durability
		String[] splitByColons = Strings.trimmedSplit(input, ":", 2);

		if (splitByColons.length > 1) {
			try {
				this.durability = NumberParser.getPositiveShort(splitByColons[1]);
			} catch (ParseException e) {
				throw new ParseException(ErrorMessages.Parsing.invalidDurability(splitByColons[1]), e);
			}

			this.hasExplicitDurability = true;

			// Only keep the first part as input
			input = splitByColons[0];
		}

		Optional<Material> material = MaterialsHelper.matchMaterial(input);

		if (!material.isPresent() || MaterialsHelper.isAir(material.get())) {
			throw new ParseException(ErrorMessages.Parsing.unknownMaterial(input));
		}
		this.material = material.get();
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
