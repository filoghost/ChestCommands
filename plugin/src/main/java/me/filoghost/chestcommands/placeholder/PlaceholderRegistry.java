/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.placeholder;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.api.PlaceholderReplacer;
import me.filoghost.chestcommands.placeholder.scanner.PlaceholderMatch;
import me.filoghost.fcommons.collection.CaseInsensitiveMap;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlaceholderRegistry {

    // <identifier, placeholder>
    private final Map<String, Placeholder> internalPlaceholders = new CaseInsensitiveMap<>();

    // <identifier, <pluginName, placeholder>>
    private final Map<String, Map<String, Placeholder>> externalPlaceholders = new CaseInsensitiveMap<>();

    public void registerInternalPlaceholder(String identifier, PlaceholderReplacer replacer) {
        internalPlaceholders.put(identifier, new Placeholder(ChestCommands.getInstance(), replacer));
    }

    public void registerExternalPlaceholder(Plugin plugin, String identifier, PlaceholderReplacer placeholderReplacer) {
        externalPlaceholders
                .computeIfAbsent(identifier, key -> new CaseInsensitiveMap<>(new LinkedHashMap<>()))
                .put(plugin.getName(), new Placeholder(plugin, placeholderReplacer));
    }

    public boolean unregisterExternalPlaceholder(Plugin plugin, String identifier) {
        Map<String, Placeholder> externalPlaceholdersByPlugin = externalPlaceholders.get(identifier);

        if (externalPlaceholdersByPlugin == null) {
            return false;
        }

        boolean removed = externalPlaceholdersByPlugin.remove(plugin.getName()) != null;

        if (externalPlaceholdersByPlugin.isEmpty()) {
            externalPlaceholders.remove(identifier);
        }

        return removed;
    }

    public @Nullable Placeholder getPlaceholder(PlaceholderMatch placeholderMatch) {
        String identifier = placeholderMatch.getIdentifier();

        if (placeholderMatch.getPluginNamespace() == null) {
            Placeholder internalPlaceholder = internalPlaceholders.get(identifier);
            if (internalPlaceholder != null) {
                return internalPlaceholder;
            }
        }

        Map<String, Placeholder> externalPlaceholdersByPlugin = externalPlaceholders.get(identifier);

        // Find exact replacer if plugin name is specified
        if (placeholderMatch.getPluginNamespace() != null) {
            return externalPlaceholdersByPlugin.get(placeholderMatch.getPluginNamespace());
        }

        // Otherwise try find the first one registered
        if (externalPlaceholdersByPlugin != null && !externalPlaceholdersByPlugin.isEmpty()) {
            return externalPlaceholdersByPlugin.values().iterator().next();
        }

        return null;
    }

}
