/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.placeholder;

import me.filoghost.chestcommands.api.PlaceholderReplacer;
import me.filoghost.chestcommands.hook.PlaceholderAPIHook;
import me.filoghost.chestcommands.placeholder.scanner.PlaceholderMatch;
import me.filoghost.chestcommands.placeholder.scanner.PlaceholderScanner;
import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.logging.Log;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderManager {

    private static final List<StaticPlaceholder> staticPlaceholders = new ArrayList<>();
    private static final PlaceholderRegistry dynamicPlaceholderRegistry = new PlaceholderRegistry();
    private static final PlaceholderCache placeholderCache = new PlaceholderCache();
    static {
        for (DefaultPlaceholder placeholder : DefaultPlaceholder.values()) {
            dynamicPlaceholderRegistry.registerInternalPlaceholder(placeholder.getIdentifier(), placeholder.getReplacer());
        }
    }

    public static boolean hasDynamicPlaceholders(List<String> list) {
        for (String element : list) {
            if (hasDynamicPlaceholders(element)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasDynamicPlaceholders(String text) {
        if (new PlaceholderScanner(text).containsAny()) {
            return true;
        }

        if (PlaceholderAPIHook.INSTANCE.isEnabled() && PlaceholderAPIHook.hasPlaceholders(text)) {
            return true;
        }

        return false;
    }

    public static String replaceDynamicPlaceholders(String text, Player player) {
        text = new PlaceholderScanner(text).replace(match -> getReplacement(match, player));

        if (PlaceholderAPIHook.INSTANCE.isEnabled()) {
            text = PlaceholderAPIHook.setPlaceholders(text, player);
        }

        return text;
    }

    private static @Nullable String getReplacement(PlaceholderMatch placeholderMatch, Player player) {
        Placeholder placeholder = dynamicPlaceholderRegistry.getPlaceholder(placeholderMatch);

        if (placeholder == null) {
            return null; // Placeholder not found
        }

        return placeholderCache.computeIfAbsent(placeholderMatch, player, () -> {
            try {
                return placeholder.getReplacer().getReplacement(player, placeholderMatch.getArgument());
            } catch (Throwable t) {
                Log.severe("Encountered an exception while replacing the placeholder \"" + placeholderMatch.getIdentifier()
                        + "\" registered by the plugin \"" + placeholder.getPlugin().getName() + "\"", t);
                return "[PLACEHOLDER ERROR]";
            }
        });
    }

    public static void setStaticPlaceholders(List<StaticPlaceholder> staticPlaceholders) {
        PlaceholderManager.staticPlaceholders.clear();
        PlaceholderManager.staticPlaceholders.addAll(staticPlaceholders);
    }

    public static boolean hasStaticPlaceholders(List<String> list) {
        for (String element : list) {
            if (hasStaticPlaceholders(element)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasStaticPlaceholders(String text) {
        for (StaticPlaceholder staticPlaceholder : staticPlaceholders) {
            if (text.contains(staticPlaceholder.getIdentifier())) {
                return true;
            }
        }
        return false;
    }

    public static String replaceStaticPlaceholders(String text) {
        for (StaticPlaceholder staticPlaceholder : staticPlaceholders) {
            text = text.replace(staticPlaceholder.getIdentifier(), staticPlaceholder.getReplacement());
        }
        return text;
    }

    public static void registerPluginPlaceholder(Plugin plugin, String identifier, PlaceholderReplacer placeholderReplacer) {
        Preconditions.notNull(plugin, "plugin");
        checkIdentifierArgument(identifier);
        Preconditions.notNull(placeholderReplacer, "placeholderReplacer");

        dynamicPlaceholderRegistry.registerExternalPlaceholder(plugin, identifier, placeholderReplacer);
    }

    public static boolean unregisterPluginPlaceholder(Plugin plugin, String identifier) {
        Preconditions.notNull(plugin, "plugin");
        checkIdentifierArgument(identifier);

        return dynamicPlaceholderRegistry.unregisterExternalPlaceholder(plugin, identifier);
    }

    private static void checkIdentifierArgument(String identifier) {
        Preconditions.notNull(identifier, "identifier");
        Preconditions.checkArgument(1 <= identifier.length() && identifier.length() <= 30, "identifier length must be between 1 and 30");
        Preconditions.checkArgument(identifier.matches("[a-zA-Z0-9_]+"), "identifier must contain only letters, numbers and underscores");
    }

    public static void onTick() {
        placeholderCache.onTick();
    }

}
