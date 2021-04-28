/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.placeholder.scanner;

import me.filoghost.fcommons.Strings;

import java.util.Objects;

public class PlaceholderMatch {

    private final String pluginNamespace;
    private final String identifier;
    private final String argument;

    private PlaceholderMatch(String pluginNamespace, String identifier, String argument) {
        this.pluginNamespace = pluginNamespace;
        this.identifier = identifier;
        this.argument = argument;
    }

    public String getPluginNamespace() {
        return pluginNamespace;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getArgument() {
        return argument;
    }

    /*
     * Valid formats:
     * {placeholder}
     * {placeholder: argument}
     * {pluginName/identifier}
     * {pluginName/identifier: argument}
     */
    public static PlaceholderMatch parse(String placeholderContent) {
        String explicitPluginName = null;
        String identifier;
        String argument = null;

        if (placeholderContent.contains(":")) {
            String[] parts = Strings.splitAndTrim(placeholderContent, ":", 2);
            identifier = parts[0];
            argument = parts[1];
        } else {
            identifier = placeholderContent;
        }

        if (identifier.contains("/")) {
            String[] parts = Strings.splitAndTrim(identifier, "/", 2);
            explicitPluginName = parts[0];
            identifier = parts[1];
        }

        return new PlaceholderMatch(explicitPluginName, identifier, argument);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        PlaceholderMatch other = (PlaceholderMatch) obj;
        return Objects.equals(this.pluginNamespace, other.pluginNamespace)
                && Objects.equals(this.identifier, other.identifier)
                && Objects.equals(this.argument, other.argument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pluginNamespace, identifier, argument);
    }

}
