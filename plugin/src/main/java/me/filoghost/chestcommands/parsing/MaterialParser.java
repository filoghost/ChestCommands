/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.parsing;

import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.fcommons.MaterialsHelper;
import org.bukkit.Material;

public class MaterialParser {

    public static Material parseMaterial(String materialName) throws ParseException {
        return MaterialsHelper.matchMaterial(materialName)
                .orElseThrow(() -> new ParseException(Errors.Parsing.unknownMaterial(materialName)));
    }

}
