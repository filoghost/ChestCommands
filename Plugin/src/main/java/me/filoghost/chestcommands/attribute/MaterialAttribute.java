/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.logging.ErrorMessages;
import me.filoghost.chestcommands.parsing.ParseException;
import me.filoghost.commons.MaterialsHelper;
import org.bukkit.Material;

import java.util.Optional;

public class MaterialAttribute implements IconAttribute {
	
	private final Material material;

	public MaterialAttribute(String serializedMaterial, AttributeErrorHandler errorHandler) throws ParseException {
		Optional<Material> material = MaterialsHelper.matchMaterial(serializedMaterial);

		if (!material.isPresent() || MaterialsHelper.isAir(material.get())) {
			throw new ParseException(ErrorMessages.Parsing.unknownMaterial(serializedMaterial));
		}

		this.material = material.get();
	}
	
	@Override
	public void apply(InternalConfigurableIcon icon) {
		icon.setMaterial(material);
	}

}
