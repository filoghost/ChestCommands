/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import java.util.ArrayList;
import java.util.List;
import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.parsing.ItemMetaParser;
import me.filoghost.chestcommands.parsing.ParseException;
import org.bukkit.block.banner.Pattern;

public class BannerPatternsAttribute implements IconAttribute {

    private final List<Pattern> patterns;

    public BannerPatternsAttribute(List<String> serializedPatterns, AttributeErrorHandler errorHandler) {
        patterns = new ArrayList<>();

        for (String serializedPattern : serializedPatterns) {
            if (serializedPattern == null || serializedPattern.isEmpty()) {
                continue; // Skip
            }

            try {
                Pattern pattern = ItemMetaParser.parseBannerPattern(serializedPattern);
                patterns.add(pattern);
            } catch (ParseException e) {
                errorHandler.onListElementError(serializedPattern, e);
            }
        }

    }
    
    @Override
    public void apply(InternalConfigurableIcon icon) {
        icon.setBannerPatterns(patterns);
    }

}
