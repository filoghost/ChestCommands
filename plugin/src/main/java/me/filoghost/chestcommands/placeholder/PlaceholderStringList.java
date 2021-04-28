/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.placeholder;

import com.google.common.collect.ImmutableList;
import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.collection.CollectionUtils;
import org.bukkit.entity.Player;

import java.util.List;

public class PlaceholderStringList {

    private final ImmutableList<String> originalList;
    private final ImmutableList<String> listWithStaticPlaceholders;
    private final ImmutableList<PlaceholderString> placeholderStringList;
    private final boolean hasDynamicPlaceholders;

    public PlaceholderStringList(List<String> list) {
        Preconditions.notNull(list, "list");
        this.originalList = ImmutableList.copyOf(list);

        // Replace static placeholders only once, if present
        if (PlaceholderManager.hasStaticPlaceholders(originalList)) {
            this.listWithStaticPlaceholders = CollectionUtils.toImmutableList(originalList, PlaceholderManager::replaceStaticPlaceholders);
        } else {
            this.listWithStaticPlaceholders = originalList;
        }

        this.hasDynamicPlaceholders = PlaceholderManager.hasDynamicPlaceholders(listWithStaticPlaceholders);
        if (hasDynamicPlaceholders) {
            this.placeholderStringList = CollectionUtils.toImmutableList(listWithStaticPlaceholders, PlaceholderString::of);
        } else {
            this.placeholderStringList = null;
        }
    }

    public ImmutableList<String> getOriginalValue() {
        return originalList;
    }

    public ImmutableList<String> getValue(Player player) {
        if (hasDynamicPlaceholders) {
            return CollectionUtils.toImmutableList(placeholderStringList, element -> element.getValue(player));
        } else {
            return listWithStaticPlaceholders;
        }
    }

    public boolean hasDynamicPlaceholders() {
        return hasDynamicPlaceholders;
    }

}
