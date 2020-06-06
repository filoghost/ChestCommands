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

@SuppressWarnings("deprecation")
public final class MaterialsRegistry {

	// Material names have been changed in 1.13, when dolphins were added
	private static final boolean USE_NEW_MATERIAL_NAMES = Utils.isClassLoaded("org.bukkit.entity.Dolphin");

	// Characters to ignore when searching materials by name
	private static final char[] IGNORE_CHARS = {'-', '_', ' '};

	// Default material names are ugly
	private static final Map<String, Material> MATERIALS_BY_ALIAS = new HashMap<>();

	// Materials that are considered air (with 1.13+ compatibility)
	private static final Collection<Material> AIR_MATERIALS = getExistingMaterials("AIR", "CAVE_AIR", "VOID_AIR");

	// Materials that have a "Sign" block state (with 1.13+ compatibility)
	private static final Collection<Material> SIGN_MATERIALS = getExistingMaterials("SIGN", "SIGN_POST", "WALL_SIGN");

	private MaterialsRegistry() {
	}

	private static void addMaterialAlias(String name, Material material) {
		MATERIALS_BY_ALIAS.put(StringUtils.stripChars(name, IGNORE_CHARS).toLowerCase(), material);
	}

	private static void tryAddMaterialAlias(String name, String materialEnumName) {
		try {
			addMaterialAlias(name, Material.valueOf(materialEnumName));
		} catch (IllegalArgumentException e) {
			// Ignore, do not add a new alias
		}
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
		Collection<Material> existingMaterials = new HashSet<Material>();

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

	public static boolean isSign(Material material) {
		return SIGN_MATERIALS.contains(material);
	}

	static {
		for (Material material : Material.values()) {
			addMaterialAlias(material.toString(), material);

			if (!USE_NEW_MATERIAL_NAMES) {
				// Add numerical IDs in versions before 1.13
				addMaterialAlias(String.valueOf(material.getId()), material);
			}
		}

		// Add some default useful aliases (when present)
		tryAddMaterialAlias("iron bar", "IRON_FENCE");
		tryAddMaterialAlias("iron bars", "IRON_FENCE");
		tryAddMaterialAlias("glass pane", "THIN_GLASS");
		tryAddMaterialAlias("nether wart", "NETHER_STALK");
		tryAddMaterialAlias("nether warts", "NETHER_STALK");
		tryAddMaterialAlias("slab", "STEP");
		tryAddMaterialAlias("double slab", "DOUBLE_STEP");
		tryAddMaterialAlias("stone brick", "SMOOTH_BRICK");
		tryAddMaterialAlias("stone bricks", "SMOOTH_BRICK");
		tryAddMaterialAlias("stone stair", "SMOOTH_STAIRS");
		tryAddMaterialAlias("stone stairs", "SMOOTH_STAIRS");
		tryAddMaterialAlias("potato", "POTATO_ITEM");
		tryAddMaterialAlias("carrot", "CARROT_ITEM");
		tryAddMaterialAlias("brewing stand", "BREWING_STAND_ITEM");
		tryAddMaterialAlias("cauldron", "CAULDRON_ITEM");
		tryAddMaterialAlias("carrot on stick", "CARROT_STICK");
		tryAddMaterialAlias("carrot on a stick", "CARROT_STICK");
		tryAddMaterialAlias("cobblestone wall", "COBBLE_WALL");
		tryAddMaterialAlias("acacia wood stairs", "ACACIA_STAIRS");
		tryAddMaterialAlias("dark oak wood stairs", "DARK_OAK_STAIRS");
		tryAddMaterialAlias("wood slab", "WOOD_STEP");
		tryAddMaterialAlias("double wood slab", "WOOD_DOUBLE_STEP");
		tryAddMaterialAlias("repeater", "DIODE");
		tryAddMaterialAlias("piston", "PISTON_BASE");
		tryAddMaterialAlias("sticky piston", "PISTON_STICKY_BASE");
		tryAddMaterialAlias("flower pot", "FLOWER_POT_ITEM");
		tryAddMaterialAlias("wood showel", "WOOD_SPADE");
		tryAddMaterialAlias("stone showel", "STONE_SPADE");
		tryAddMaterialAlias("gold showel", "GOLD_SPADE");
		tryAddMaterialAlias("iron showel", "IRON_SPADE");
		tryAddMaterialAlias("diamond showel", "DIAMOND_SPADE");
		tryAddMaterialAlias("steak", "COOKED_BEEF");
		tryAddMaterialAlias("cooked porkchop", "GRILLED_PORK");
		tryAddMaterialAlias("raw porkchop", "PORK");
		tryAddMaterialAlias("hardened clay", "HARD_CLAY");
		tryAddMaterialAlias("huge brown mushroom", "HUGE_MUSHROOM_1");
		tryAddMaterialAlias("huge red mushroom", "HUGE_MUSHROOM_2");
		tryAddMaterialAlias("mycelium", "MYCEL");
		tryAddMaterialAlias("poppy", "RED_ROSE");
		tryAddMaterialAlias("comparator", "REDSTONE_COMPARATOR");
		tryAddMaterialAlias("skull", "SKULL_ITEM");
		tryAddMaterialAlias("head", "SKULL_ITEM");
		tryAddMaterialAlias("redstone torch", "REDSTONE_TORCH_ON");
		tryAddMaterialAlias("redstone lamp", "REDSTONE_LAMP_OFF");
		tryAddMaterialAlias("glistering melon", "SPECKLED_MELON");
		tryAddMaterialAlias("acacia leaves", "LEAVES_2");
		tryAddMaterialAlias("acacia log", "LOG_2");
		tryAddMaterialAlias("gunpowder", "SULPHUR");
		tryAddMaterialAlias("lilypad", "WATER_LILY");
		tryAddMaterialAlias("command block", "COMMAND");
		tryAddMaterialAlias("dye", "INK_SACK");
	}

}
