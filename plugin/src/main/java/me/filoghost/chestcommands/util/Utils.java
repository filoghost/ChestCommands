/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.util;

import me.filoghost.fcommons.Strings;
import org.jetbrains.annotations.NotNull;

public class Utils {

    public static String formatEnum(@NotNull Enum<?> enumValue) {
        return Strings.capitalizeFully(enumValue.name().replace("_", " "));
    }

    public static String addYamlExtension(@NotNull String fileName) {
        if (fileName.toLowerCase().endsWith(".yml")) {
            return fileName;
        } else {
            return fileName + ".yml";
        }
    }

}
