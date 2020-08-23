/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy.upgrade;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexReplacer implements Function<String, String> {

    private final Pattern regex;
    private final Function<Matcher, String> replaceCallback;

    public RegexReplacer(Pattern regex, Function<Matcher, String> replaceCallback) {
        this.regex = regex;
        this.replaceCallback = replaceCallback;
    }

    @Override
    public String apply(String string) {
        Matcher matcher = regex.matcher(string);
        StringBuffer output = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(output, replaceCallback.apply(matcher));
        }
        matcher.appendTail(output);

        return output.toString();
    }
}
