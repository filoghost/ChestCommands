/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.parsing;

import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.fcommons.Strings;
import me.filoghost.fcommons.collection.EnumLookupRegistry;
import me.filoghost.fcommons.collection.LookupRegistry;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

public final class ItemMetaParser {

    private static final LookupRegistry<DyeColor> DYE_COLORS_REGISTRY = EnumLookupRegistry.fromEnumValues(DyeColor.class);
    private static final LookupRegistry<PatternType> PATTERN_TYPES_REGISTRY = EnumLookupRegistry.fromEnumValues(PatternType.class);

    private ItemMetaParser() {}


    public static Color parseRGBColor(String input) throws ParseException {
        String[] split = Strings.splitAndTrim(input, ",");

        if (split.length != 3) {
            throw new ParseException(Errors.Parsing.invalidColorFormat);
        }

        int red = parseColor(split[0], "red");
        int green = parseColor(split[1], "green");
        int blue = parseColor(split[2], "blue");

        return Color.fromRGB(red, green, blue);
    }

    private static int parseColor(String valueString, String colorName) throws ParseException {
        int value;

        try {
            value = NumberParser.getInteger(valueString);
        } catch (ParseException e) {
            throw new ParseException(Errors.Parsing.invalidColorNumber(valueString, colorName), e);
        }

        if (value < 0 || value > 255) {
            throw new ParseException(Errors.Parsing.invalidColorRange(valueString, colorName));
        }

        return value;
    }

    public static DyeColor parseDyeColor(String input) throws ParseException {
        DyeColor dyeColor = DYE_COLORS_REGISTRY.lookup(input);
        if (dyeColor == null) {
            throw new ParseException(Errors.Parsing.unknownDyeColor(input));
        }
        return dyeColor;
    }

    public static Pattern parseBannerPattern(String input) throws ParseException {
        String[] split = Strings.splitAndTrim(input, ":");
        if (split.length != 2) {
            throw new ParseException(Errors.Parsing.invalidPatternFormat);
        }

        PatternType patternType = PATTERN_TYPES_REGISTRY.lookup(split[0]);
        if (patternType == null) {
            throw new ParseException(Errors.Parsing.unknownPatternType(split[0]));
        }
        DyeColor patternColor = parseDyeColor(split[1]);

        return new Pattern(patternColor, patternType);
    }
}
