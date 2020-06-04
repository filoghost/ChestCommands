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

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.filoghost.chestcommands.exception.FormatException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class ItemUtils {

	private static final boolean USE_ITEM_FLAGS_API;
	private static final boolean USE_ITEM_FLAGS_REFLECTION;

	// Reflection stuff
	private static Class<?> nbtTagCompoundClass;
	private static Class<?> nbtTagListClass;
	private static Class<?> nmsItemstackClass;
	private static Method asNmsCopyMethod;
	private static Method asCraftMirrorMethod;
	private static Method hasTagMethod;
	private static Method getTagMethod;
	private static Method setTagMethod;
	private static Method nbtSetMethod;

	static  {
		if (Utils.isClassLoaded("org.bukkit.inventory.ItemFlag")) {
			// We can use the new Bukkit API (1.8.3+)
			USE_ITEM_FLAGS_API = true;
			USE_ITEM_FLAGS_REFLECTION = false;

		} else {
			USE_ITEM_FLAGS_API = false;
			// Try to get the NMS methods and classes
			boolean success;
			try {
				nbtTagCompoundClass = NMSUtils.getNMSClass("NBTTagCompound");
				nbtTagListClass = NMSUtils.getNMSClass("NBTTagList");
				nmsItemstackClass = NMSUtils.getNMSClass("ItemStack");

				asNmsCopyMethod = NMSUtils.getCraftBukkitClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class);
				asCraftMirrorMethod = NMSUtils.getCraftBukkitClass("inventory.CraftItemStack").getMethod("asCraftMirror", nmsItemstackClass);

				hasTagMethod = nmsItemstackClass.getMethod("hasTag");
				getTagMethod = nmsItemstackClass.getMethod("getTag");
				setTagMethod = nmsItemstackClass.getMethod("setTag", nbtTagCompoundClass);

				nbtSetMethod = nbtTagCompoundClass.getMethod("set", String.class, NMSUtils.getNMSClass("NBTBase"));

				success = true;
			} catch (Exception e) {
				new IllegalStateException("Could not enable the attribute remover for this version." +
						"Attributes will show up on items.", e).printStackTrace();
				success = false;
			}
			USE_ITEM_FLAGS_REFLECTION = success;
		}
	}

	private ItemUtils() {
	}

	public static ItemStack hideAttributes(ItemStack item) {
		if (item == null) {
			return null;
		}

		if (USE_ITEM_FLAGS_API) {
			ItemMeta meta = item.getItemMeta();
			if (Utils.isNullOrEmpty(meta.getItemFlags())) {
				// Add them only if necessary
				meta.addItemFlags(ItemFlag.values());
				item.setItemMeta(meta);
			}
			return item;

		} else if (USE_ITEM_FLAGS_REFLECTION) {
			try {

				Object nmsItemstack = asNmsCopyMethod.invoke(null, item);
				if (nmsItemstack == null) {
					return item;
				}

				Object nbtCompound;
				if ((Boolean) hasTagMethod.invoke(nmsItemstack)) {
					nbtCompound = getTagMethod.invoke(nmsItemstack);
				} else {
					nbtCompound = nbtTagCompoundClass.newInstance();
					setTagMethod.invoke(nmsItemstack, nbtCompound);
				}

				if (nbtCompound == null) {
					return item;
				}

				Object nbtList = nbtTagListClass.newInstance();
				nbtSetMethod.invoke(nbtCompound, "AttributeModifiers", nbtList);
				return (ItemStack) asCraftMirrorMethod.invoke(null, nmsItemstack);

			} catch (Throwable t) {
				// Ignore
			}
		}

		// On failure just return the item
		return item;
	}

	public static Color parseColor(String input) throws FormatException {
		String[] split = StringUtils.stripChars(input, " ").split(",");

		if (split.length != 3) {
			throw new FormatException("it must be in the format \"red, green, blue\".");
		}

		int red, green, blue;

		try {
			red = Integer.parseInt(split[0]);
			green = Integer.parseInt(split[1]);
			blue = Integer.parseInt(split[2]);
		} catch (NumberFormatException ex) {
			throw new FormatException("it contains invalid numbers.");
		}

		if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
			throw new FormatException("it should only contain numbers between 0 and 255.");
		}

		return Color.fromRGB(red, green, blue);
	}

	public static DyeColor parseDyeColor(String input) throws FormatException {
		DyeColor color;
		try {
			color = DyeColor.valueOf(input.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new FormatException("it must be a valid color.");
		}
		return color;
	}

	public static List<Pattern> parseBannerPatternList(List<String> input) throws FormatException {
		List<Pattern> patterns = new ArrayList<Pattern>();
		for (String str : input) {
			String[] split = str.split(":");
			if (split.length != 2) {
				throw new FormatException("it must be in the format \"pattern:color\".");
			}
			try {
				patterns.add(new Pattern(parseDyeColor(split[1]), PatternType.valueOf(split[0].toUpperCase())));
			} catch (IllegalArgumentException e) {
				throw new FormatException("it must be a valid pattern type.");
			}
		}
		return patterns;
	}
}
