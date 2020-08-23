/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.parsing;

import me.filoghost.chestcommands.logging.Errors;

public class NumberParser {

    public static double getStrictlyPositiveDouble(String input) throws ParseException {
        double value = getDouble(input);
        check(value > 0.0, Errors.Parsing.strictlyPositive);
        return value;
    }

    private static double getDouble(String input) throws ParseException {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException ex) {
            throw new ParseException(Errors.Parsing.invalidDecimal);
        }
    }

    public static float getFloat(String input) throws ParseException {
        try {
            return Float.parseFloat(input);
        } catch (NumberFormatException ex) {
            throw new ParseException(Errors.Parsing.invalidDecimal);
        }
    }

    public static short getPositiveShort(String input) throws ParseException {
        short value = getShort(input);
        check(value >= 0, Errors.Parsing.zeroOrPositive);
        return value;
    }

    private static short getShort(String input) throws ParseException {
        try {
            return Short.parseShort(input);
        } catch (NumberFormatException ex) {
            throw new ParseException(Errors.Parsing.invalidShort);
        }
    }

    public static int getStrictlyPositiveInteger(String input) throws ParseException {
        int value = getInteger(input);
        check(value > 0, Errors.Parsing.strictlyPositive);
        return value;
    }

    public static int getInteger(String input) throws ParseException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            throw new ParseException(Errors.Parsing.invalidInteger);
        }
    }

    private static void check(boolean expression, String errorMessage) throws ParseException {
        if (!expression) {
            throw new ParseException(errorMessage);
        }
    }
}
