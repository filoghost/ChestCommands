/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.placeholder;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Supplier;
import me.filoghost.chestcommands.placeholder.scanner.PlaceholderMatch;
import org.bukkit.entity.Player;

public class PlaceholderCache {

    private final Map<Player, Map<PlaceholderMatch, String>> cachedReplacements;

    public PlaceholderCache() {
        cachedReplacements = new WeakHashMap<>();
    }

    public String computeIfAbsent(PlaceholderMatch placeholderMatch, Player player, Supplier<String> replacementGetter) {
        return cachedReplacements
                .computeIfAbsent(player, key -> new HashMap<>())
                .computeIfAbsent(placeholderMatch, key -> replacementGetter.get());
    }

    public void onTick() {
        cachedReplacements.forEach((player, placeholderMap) -> placeholderMap.clear());
    }

}
