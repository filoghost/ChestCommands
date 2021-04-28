/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.placeholder;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.api.PlaceholderReplacer;
import me.filoghost.chestcommands.placeholder.scanner.PlaceholderMatch;
import me.filoghost.fcommons.collection.CaseInsensitiveHashMap;
import me.filoghost.fcommons.collection.CaseInsensitiveLinkedHashMap;
import me.filoghost.fcommons.collection.CaseInsensitiveMap;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class PlaceholderRegistry {

    // <identifier, placeholder>
    private final CaseInsensitiveMap<Placeholder> internalPlaceholders = new CaseInsensitiveHashMap<>();

    // <identifier, <pluginName, placeholder>>
    private final CaseInsensitiveMap<CaseInsensitiveMap<Placeholder>> externalPlaceholders = new CaseInsensitiveHashMap<>();

    public void registerInternalPlaceholder(String identifier, PlaceholderReplacer replacer) {
        internalPlaceholders.put(identifier, new Placeholder(ChestCommands.getInstance(), replacer));
    }

    public void registerExternalPlaceholder(Plugin plugin, String identifier, PlaceholderReplacer placeholderReplacer) {
        externalPlaceholders
                .computeIfAbsent(identifier, CaseInsensitiveLinkedHashMap::new)
                .put(plugin.getName(), new Placeholder(plugin, placeholderReplacer));
    }

    public boolean unregisterExternalPlaceholder(Plugin plugin, String identifier) {
        CaseInsensitiveMap<Placeholder> externalPlaceholdersByPlugin = externalPlaceholders.get(identifier);

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

        CaseInsensitiveMap<Placeholder> externalPlaceholdersByPlugin = externalPlaceholders.get(identifier);
        if (externalPlaceholdersByPlugin == null) {
            return null;
        }

        // Find exact replacer if plugin name is specified
        if (placeholderMatch.getPluginNamespace() != null) {
            return externalPlaceholdersByPlugin.get(placeholderMatch.getPluginNamespace());
        }

        // Otherwise try find the first one registered
        if (!externalPlaceholdersByPlugin.isEmpty()) {
            return externalPlaceholdersByPlugin.values().iterator().next();
        }

        return null;
    }

}
