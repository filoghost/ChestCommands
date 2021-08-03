/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.parsing;

import me.filoghost.chestcommands.logging.Errors.Parsing;
import me.filoghost.fcommons.MaterialsHelper;
import org.bukkit.Material;

public class MaterialParser {

    public static Material parseMaterial(String materialName) throws ParseException {
        Material material = MaterialsHelper.matchMaterial(materialName);
        if (material == null) {
            throw new ParseException(Parsing.unknownMaterial(materialName));
        }
        return material;
    }

}
