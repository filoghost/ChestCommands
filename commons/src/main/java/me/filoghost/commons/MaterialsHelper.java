/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.commons;

import me.filoghost.commons.collection.Registry;
import org.bukkit.Material;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

public final class MaterialsHelper {

	// Registry of materials by numerical ID (before 1.13), name and aliases
	private static final Registry<Material> MATERIALS_REGISTRY = initMaterialsRegistry();

	// Materials that are considered air (with 1.13+ compatibility)
	private static final Collection<Material> AIR_MATERIALS = getExistingMaterials("AIR", "CAVE_AIR", "VOID_AIR");


	@SuppressWarnings("deprecation")
	private static Registry<Material> initMaterialsRegistry() {
		Registry<Material> materialsRegistry = Registry.fromEnumValues(Material.class);

		// Add numerical IDs in legacy versions (the Tag class was added in 1.13)
		if (!CommonsUtil.isClassLoaded("org.bukkit.Tag")) {
			for (Material material : Material.values()) {
				materialsRegistry.put(Integer.toString(material.getId()), material);
			}
		}

		// Add some default useful aliases (when present)
		materialsRegistry.putIfEnumExists("iron bar", "IRON_FENCE");
		materialsRegistry.putIfEnumExists("iron bars", "IRON_FENCE");
		materialsRegistry.putIfEnumExists("glass pane", "THIN_GLASS");
		materialsRegistry.putIfEnumExists("nether wart", "NETHER_STALK");
		materialsRegistry.putIfEnumExists("nether warts", "NETHER_STALK");
		materialsRegistry.putIfEnumExists("slab", "STEP");
		materialsRegistry.putIfEnumExists("double slab", "DOUBLE_STEP");
		materialsRegistry.putIfEnumExists("stone brick", "SMOOTH_BRICK");
		materialsRegistry.putIfEnumExists("stone bricks", "SMOOTH_BRICK");
		materialsRegistry.putIfEnumExists("stone stair", "SMOOTH_STAIRS");
		materialsRegistry.putIfEnumExists("stone stairs", "SMOOTH_STAIRS");
		materialsRegistry.putIfEnumExists("potato", "POTATO_ITEM");
		materialsRegistry.putIfEnumExists("carrot", "CARROT_ITEM");
		materialsRegistry.putIfEnumExists("brewing stand", "BREWING_STAND_ITEM");
		materialsRegistry.putIfEnumExists("cauldron", "CAULDRON_ITEM");
		materialsRegistry.putIfEnumExists("carrot on stick", "CARROT_STICK");
		materialsRegistry.putIfEnumExists("carrot on a stick", "CARROT_STICK");
		materialsRegistry.putIfEnumExists("cobblestone wall", "COBBLE_WALL");
		materialsRegistry.putIfEnumExists("acacia wood stairs", "ACACIA_STAIRS");
		materialsRegistry.putIfEnumExists("dark oak wood stairs", "DARK_OAK_STAIRS");
		materialsRegistry.putIfEnumExists("wood slab", "WOOD_STEP");
		materialsRegistry.putIfEnumExists("double wood slab", "WOOD_DOUBLE_STEP");
		materialsRegistry.putIfEnumExists("repeater", "DIODE");
		materialsRegistry.putIfEnumExists("piston", "PISTON_BASE");
		materialsRegistry.putIfEnumExists("sticky piston", "PISTON_STICKY_BASE");
		materialsRegistry.putIfEnumExists("flower pot", "FLOWER_POT_ITEM");
		materialsRegistry.putIfEnumExists("wood showel", "WOOD_SPADE");
		materialsRegistry.putIfEnumExists("stone showel", "STONE_SPADE");
		materialsRegistry.putIfEnumExists("gold showel", "GOLD_SPADE");
		materialsRegistry.putIfEnumExists("iron showel", "IRON_SPADE");
		materialsRegistry.putIfEnumExists("diamond showel", "DIAMOND_SPADE");
		materialsRegistry.putIfEnumExists("steak", "COOKED_BEEF");
		materialsRegistry.putIfEnumExists("cooked porkchop", "GRILLED_PORK");
		materialsRegistry.putIfEnumExists("raw porkchop", "PORK");
		materialsRegistry.putIfEnumExists("hardened clay", "HARD_CLAY");
		materialsRegistry.putIfEnumExists("huge brown mushroom", "HUGE_MUSHROOM_1");
		materialsRegistry.putIfEnumExists("huge red mushroom", "HUGE_MUSHROOM_2");
		materialsRegistry.putIfEnumExists("mycelium", "MYCEL");
		materialsRegistry.putIfEnumExists("poppy", "RED_ROSE");
		materialsRegistry.putIfEnumExists("comparator", "REDSTONE_COMPARATOR");
		materialsRegistry.putIfEnumExists("skull", "SKULL_ITEM");
		materialsRegistry.putIfEnumExists("head", "SKULL_ITEM");
		materialsRegistry.putIfEnumExists("redstone torch", "REDSTONE_TORCH_ON");
		materialsRegistry.putIfEnumExists("redstone lamp", "REDSTONE_LAMP_OFF");
		materialsRegistry.putIfEnumExists("glistering melon", "SPECKLED_MELON");
		materialsRegistry.putIfEnumExists("acacia leaves", "LEAVES_2");
		materialsRegistry.putIfEnumExists("acacia log", "LOG_2");
		materialsRegistry.putIfEnumExists("gunpowder", "SULPHUR");
		materialsRegistry.putIfEnumExists("lilypad", "WATER_LILY");
		materialsRegistry.putIfEnumExists("command block", "COMMAND");
		materialsRegistry.putIfEnumExists("dye", "INK_SACK");
		
		return materialsRegistry;
	}

	public static Optional<Material> matchMaterial(String materialName) {
		return MATERIALS_REGISTRY.find(materialName);
	}

	private static Collection<Material> getExistingMaterials(String... materialEnumNames) {
		Collection<Material> existingMaterials = new HashSet<>();

		for (String materialEnumName : materialEnumNames) {
			try {
				existingMaterials.add(Material.valueOf(materialEnumName));
			} catch (IllegalArgumentException e) {
				// Ignore, not existing
			}
		}

		return existingMaterials;
	}

	public static boolean isAir(Material material) {
		return AIR_MATERIALS.contains(material);
	}

}
