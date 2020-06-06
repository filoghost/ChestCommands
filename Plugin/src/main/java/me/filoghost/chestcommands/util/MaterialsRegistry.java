/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.chestcommands.util;

import org.bukkit.Material;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public final class MaterialsRegistry {

	// Material names have been changed in 1.13, when dolphins were added
	private static final boolean USE_NEW_MATERIAL_NAMES = Utils.isClassLoaded("org.bukkit.entity.Dolphin");

	// Characters to ignore when searching materials by name
	private static final char[] IGNORE_CHARS = {'-', '_', ' '};

	// Default material names are ugly
	private static final Map<String, Material> MATERIALS_BY_ALIAS = initMaterialsByAlias();

	// Materials that are considered air (with 1.13+ compatibility)
	private static final Collection<Material> AIR_MATERIALS = getExistingMaterials("AIR", "CAVE_AIR", "VOID_AIR");

	private MaterialsRegistry() {}
	
	@SuppressWarnings("deprecation")
	private static Map<String, Material> initMaterialsByAlias() {
		Map<String, Material> materialsByAlias = new HashMap<>();
		
		for (Material material : Material.values()) {
			addMaterialAlias(materialsByAlias, material.toString(), material);

			if (!USE_NEW_MATERIAL_NAMES) {
				// Add numerical IDs in versions before 1.13
				addMaterialAlias(materialsByAlias, String.valueOf(material.getId()), material);
			}
		}

		// Add some default useful aliases (when present)
		tryAddMaterialAlias(materialsByAlias, "iron bar", "IRON_FENCE");
		tryAddMaterialAlias(materialsByAlias, "iron bars", "IRON_FENCE");
		tryAddMaterialAlias(materialsByAlias, "glass pane", "THIN_GLASS");
		tryAddMaterialAlias(materialsByAlias, "nether wart", "NETHER_STALK");
		tryAddMaterialAlias(materialsByAlias, "nether warts", "NETHER_STALK");
		tryAddMaterialAlias(materialsByAlias, "slab", "STEP");
		tryAddMaterialAlias(materialsByAlias, "double slab", "DOUBLE_STEP");
		tryAddMaterialAlias(materialsByAlias, "stone brick", "SMOOTH_BRICK");
		tryAddMaterialAlias(materialsByAlias, "stone bricks", "SMOOTH_BRICK");
		tryAddMaterialAlias(materialsByAlias, "stone stair", "SMOOTH_STAIRS");
		tryAddMaterialAlias(materialsByAlias, "stone stairs", "SMOOTH_STAIRS");
		tryAddMaterialAlias(materialsByAlias, "potato", "POTATO_ITEM");
		tryAddMaterialAlias(materialsByAlias, "carrot", "CARROT_ITEM");
		tryAddMaterialAlias(materialsByAlias, "brewing stand", "BREWING_STAND_ITEM");
		tryAddMaterialAlias(materialsByAlias, "cauldron", "CAULDRON_ITEM");
		tryAddMaterialAlias(materialsByAlias, "carrot on stick", "CARROT_STICK");
		tryAddMaterialAlias(materialsByAlias, "carrot on a stick", "CARROT_STICK");
		tryAddMaterialAlias(materialsByAlias, "cobblestone wall", "COBBLE_WALL");
		tryAddMaterialAlias(materialsByAlias, "acacia wood stairs", "ACACIA_STAIRS");
		tryAddMaterialAlias(materialsByAlias, "dark oak wood stairs", "DARK_OAK_STAIRS");
		tryAddMaterialAlias(materialsByAlias, "wood slab", "WOOD_STEP");
		tryAddMaterialAlias(materialsByAlias, "double wood slab", "WOOD_DOUBLE_STEP");
		tryAddMaterialAlias(materialsByAlias, "repeater", "DIODE");
		tryAddMaterialAlias(materialsByAlias, "piston", "PISTON_BASE");
		tryAddMaterialAlias(materialsByAlias, "sticky piston", "PISTON_STICKY_BASE");
		tryAddMaterialAlias(materialsByAlias, "flower pot", "FLOWER_POT_ITEM");
		tryAddMaterialAlias(materialsByAlias, "wood showel", "WOOD_SPADE");
		tryAddMaterialAlias(materialsByAlias, "stone showel", "STONE_SPADE");
		tryAddMaterialAlias(materialsByAlias, "gold showel", "GOLD_SPADE");
		tryAddMaterialAlias(materialsByAlias, "iron showel", "IRON_SPADE");
		tryAddMaterialAlias(materialsByAlias, "diamond showel", "DIAMOND_SPADE");
		tryAddMaterialAlias(materialsByAlias, "steak", "COOKED_BEEF");
		tryAddMaterialAlias(materialsByAlias, "cooked porkchop", "GRILLED_PORK");
		tryAddMaterialAlias(materialsByAlias, "raw porkchop", "PORK");
		tryAddMaterialAlias(materialsByAlias, "hardened clay", "HARD_CLAY");
		tryAddMaterialAlias(materialsByAlias, "huge brown mushroom", "HUGE_MUSHROOM_1");
		tryAddMaterialAlias(materialsByAlias, "huge red mushroom", "HUGE_MUSHROOM_2");
		tryAddMaterialAlias(materialsByAlias, "mycelium", "MYCEL");
		tryAddMaterialAlias(materialsByAlias, "poppy", "RED_ROSE");
		tryAddMaterialAlias(materialsByAlias, "comparator", "REDSTONE_COMPARATOR");
		tryAddMaterialAlias(materialsByAlias, "skull", "SKULL_ITEM");
		tryAddMaterialAlias(materialsByAlias, "head", "SKULL_ITEM");
		tryAddMaterialAlias(materialsByAlias, "redstone torch", "REDSTONE_TORCH_ON");
		tryAddMaterialAlias(materialsByAlias, "redstone lamp", "REDSTONE_LAMP_OFF");
		tryAddMaterialAlias(materialsByAlias, "glistering melon", "SPECKLED_MELON");
		tryAddMaterialAlias(materialsByAlias, "acacia leaves", "LEAVES_2");
		tryAddMaterialAlias(materialsByAlias, "acacia log", "LOG_2");
		tryAddMaterialAlias(materialsByAlias, "gunpowder", "SULPHUR");
		tryAddMaterialAlias(materialsByAlias, "lilypad", "WATER_LILY");
		tryAddMaterialAlias(materialsByAlias, "command block", "COMMAND");
		tryAddMaterialAlias(materialsByAlias, "dye", "INK_SACK");
		
		return materialsByAlias;
	}

	private static void tryAddMaterialAlias(Map<String, Material> materialsByAlias, String name, String materialEnumName) {
		try {
			addMaterialAlias(materialsByAlias, name, Material.valueOf(materialEnumName));
		} catch (IllegalArgumentException e) {
			// Ignore, do not add a new alias
		}
	}
	
	private static void addMaterialAlias(Map<String, Material> materialsByAlias, String name, Material material) {
		materialsByAlias.put(StringUtils.stripChars(name, IGNORE_CHARS).toLowerCase(), material);
	}

	public static Material matchMaterial(String alias) {
		if (alias == null) {
			return null;
		}

		return MATERIALS_BY_ALIAS.get(StringUtils.stripChars(alias, IGNORE_CHARS).toLowerCase());
	}

	public static String formatMaterial(Material material) {
		return StringUtils.capitalizeFully(material.toString().replace("_", " "));
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
