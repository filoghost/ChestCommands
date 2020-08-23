/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.icon.requirement.item;

import me.filoghost.fcommons.Preconditions;
import org.bukkit.Material;

public class RequiredItem {

    private final Material material;
    private final int amount;
    private short durability;
    private boolean isDurabilityRestrictive = false;

    public RequiredItem(Material material, int amount) {
        Preconditions.checkArgumentNotAir(material, "material");

        this.material = material;
        this.amount = amount;
    }

    public Material getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    public short getDurability() {
        return durability;
    }

    public void setRestrictiveDurability(short durability) {
        Preconditions.checkArgument(durability >= 0, "durability must be 0 or greater");

        this.durability = durability;
        isDurabilityRestrictive = true;
    }

    public boolean hasRestrictiveDurability() {
        return isDurabilityRestrictive;
    }

    public boolean isMatchingType(RemainingItem item) {
        return item != null && item.getMaterial() == material && isMatchingDurability(item.getDurability());
    }
    
    private boolean isMatchingDurability(short durability) {
        if (isDurabilityRestrictive) {
            return this.durability == durability;
        } else {
            return true;
        }
    }
    
}
