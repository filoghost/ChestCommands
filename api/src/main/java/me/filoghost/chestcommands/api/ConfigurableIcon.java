/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.api;

import me.filoghost.chestcommands.api.internal.BackendAPI;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * An {@link Icon} that displays an {@link ItemStack} based on its configurable properties.
 *
 * @since 1
 */
public interface ConfigurableIcon extends ClickableIcon {

    /**
     * Creates a new configurable icon with a given material.
     *
     * @param material the material
     * @return the created icon
     * @since 1
     */
    static @NotNull ConfigurableIcon create(@NotNull Material material) {
        return BackendAPI.getImplementation().createConfigurableIcon(material);
    }

    /**
     * Sets the material of the displayed item.
     *
     * @param material the new material
     * @see ItemStack#setType(Material)
     * @since 1
     */
    void setMaterial(@NotNull Material material);

    /**
     * Returns the material of the displayed item.
     *
     * @return the current material
     * @see ItemStack#getType()
     * @since 1
     */
    @NotNull Material getMaterial();

    /**
     * Sets the amount of the displayed item.
     *
     * @param amount the new amount
     * @see ItemStack#setAmount(int)
     * @since 1
     */
    void setAmount(int amount);

    /**
     * Returns the amount of the displayed item.
     *
     * @return the current amount
     * @see ItemStack#getAmount()
     * @since 1
     */
    int getAmount();

    /**
     * Sets the durability of the displayed item.
     *
     * @param durability the new durability
     * @see ItemStack#setDurability(short)
     * @since 1
     */
    void setDurability(short durability);

    /**
     * Returns the durability of the displayed item.
     *
     * @return the current durability
     * @see ItemStack#getDurability()
     * @since 1
     */
    short getDurability();

    /**
     * Sets the NBT data of the displayed item. When creating the displayed item, all other options have higher priority
     * and override NBT values (for example, {@link ConfigurableIcon#setName(String)} overrides the name inside NBT
     * data).
     *
     * @param nbtData the new NBT data as JSON string, null to remove
     * @throws IllegalArgumentException if the NBT data is not valid
     * @since 1
     */
    void setNBTData(@Nullable String nbtData);

    /**
     * Returns the NBT data of the displayed item.
     *
     * @return the current NBT data as JSON string, null if absent
     * @see ConfigurableIcon#setNBTData(String)
     * @since 1
     */
    @Nullable String getNBTData();

    /**
     * Sets the name, which is the first line in the tooltip of the displayed item. Can contain colors.
     *
     * @param name the new name, null to remove
     * @since 1
     */
    void setName(@Nullable String name);

    /**
     * Returns the name.
     *
     * @return the current name, null if absent
     * @see ConfigurableIcon#setName(String)
     * @since 1
     */
    @Nullable String getName();

    /**
     * Sets the lore, which is the lines below the name in the tooltip of the displayed item. Can contain colors.
     *
     * @param lore the new lore as list of lines, null to remove
     * @since 1
     */
    void setLore(@Nullable List<String> lore);

    /**
     * Sets the lore.
     *
     * @param lore the new lore as array of lines, null to remove
     * @see ConfigurableIcon#setLore(List)
     * @since 1
     */
    void setLore(@Nullable String... lore);

    /**
     * Returns the lore.
     *
     * @return the current lore, null if absent
     * @see ConfigurableIcon#setLore(List)
     * @since 1
     */
    @Nullable List<String> getLore();

    /**
     * Sets the enchantments of the displayed item, removing existing ones.
     *
     * @param enchantments the new enchantments, as map of enchantment and level, null to remove all enchantments
     * @since 1
     */
    void setEnchantments(@Nullable Map<Enchantment, Integer> enchantments);

    /**
     * Returns the enchantments of the displayed item.
     *
     * @return the current enchantments, as map of enchantment and level, null if absent
     * @see ConfigurableIcon#setEnchantments(Map)
     * @since 1
     */
    @Nullable Map<Enchantment, Integer> getEnchantments();

    /**
     * Adds a level 1 enchantment to the displayed item.
     *
     * @param enchantment the enchantment to add
     * @since 1
     */
    void addEnchantment(@NotNull Enchantment enchantment);

    /**
     * Adds an enchantment to the displayed item.
     *
     * @param enchantment the enchantment to add
     * @param level       the level of the enchantment
     * @since 1
     */
    void addEnchantment(@NotNull Enchantment enchantment, int level);

    /**
     * Removes an enchantment from the displayed item.
     *
     * @param enchantment the enchantment to remove
     * @since 1
     */
    void removeEnchantment(@NotNull Enchantment enchantment);

    /**
     * Sets the leather color of the displayed leather armor.
     * <p>
     * This value is ignored if the material is not a leather armor piece.
     *
     * @param leatherColor the new leather color, null to remove
     * @since 1
     */
    void setLeatherColor(@Nullable Color leatherColor);

    /**
     * Returns the leather color of the displayed item.
     *
     * @return the current leather color, null if absent
     * @see ConfigurableIcon#setLeatherColor(Color)
     * @since 1
     */
    @Nullable Color getLeatherColor();

    /**
     * Sets the skull owner of the displayed player head.
     * <p>
     * This value is ignored if the material is not a player head.
     *
     * @param skullOwner the new skull owner, null to remove
     * @since 1
     */
    void setSkullOwner(@Nullable String skullOwner);

    /**
     * Returns the skull owner.
     *
     * @return the current skull owner, null if absent
     * @see ConfigurableIcon#setSkullOwner(String)
     * @since 1
     */
    @Nullable String getSkullOwner();

    /**
     * Sets the color of the displayed banner. <b>Only used before Minecraft 1.13.</b>
     * <p>
     * This value is ignored if the material is not a banner.
     *
     * @param bannerColor the new banner color, null to remove
     * @since 1
     */
    void setBannerColor(@Nullable DyeColor bannerColor);

    /**
     * Returns the banner color.
     *
     * @return the current banner color, null if absent
     * @see ConfigurableIcon#setBannerColor(DyeColor)
     * @since 1
     */
    @Nullable DyeColor getBannerColor();

    /**
     * Sets the patterns of the displayed banner.
     * <p>
     * This value is ignored if the material is not a banner.
     *
     * @param bannerPatterns the new banner patterns list, null to remove
     * @since 1
     */
    void setBannerPatterns(@Nullable List<Pattern> bannerPatterns);

    /**
     * Sets the patterns of the displayed banner.
     *
     * @param bannerPatterns the new banner patterns array, null to remove
     * @see ConfigurableIcon#setBannerPatterns(Pattern...)
     * @since 1
     */
    void setBannerPatterns(@Nullable Pattern... bannerPatterns);

    /**
     * Returns the patterns of the displayed banner.
     *
     * @return the current banner patterns, null if absent
     * @see ConfigurableIcon#setBannerPatterns(List)
     * @since 1
     */
    @Nullable List<Pattern> getBannerPatterns();

    /**
     * Sets if placeholders should be enabled.
     *
     * @param enabled true if the placeholders should be enabled, false otherwise
     * @since 1
     */
    void setPlaceholdersEnabled(boolean enabled);

    /**
     * Returns if placeholders are currently enabled. By default they are not enabled.
     *
     * @return true if placeholders are currently enabled, false otherwise
     * @since 1
     */
    boolean isPlaceholdersEnabled();

}
