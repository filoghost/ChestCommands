package com.gmail.filoghost.chestcommands.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.chestcommands.ChestCommands;
import com.gmail.filoghost.chestcommands.exception.FormatException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Utils {
	
	// Default material names are ugly.
	private static Map<String, Material> materialMap = Maps.newHashMap();
	static {
		for (Material mat : Material.values()) {
			materialMap.put(StringUtils.stripChars(mat.toString(), "_").toLowerCase(), mat);
		}
		
		Map<String, Material> tempMap = Maps.newHashMap();
		
		tempMap.put("iron bar",				Material.IRON_FENCE);
		tempMap.put("iron bars",			Material.IRON_FENCE);
		tempMap.put("glass pane",			Material.THIN_GLASS);
		tempMap.put("nether wart",			Material.NETHER_STALK);
		tempMap.put("nether warts",			Material.NETHER_STALK);
		tempMap.put("slab",					Material.STEP);
		tempMap.put("double slab",			Material.DOUBLE_STEP);
		tempMap.put("stone brick",			Material.SMOOTH_BRICK);
		tempMap.put("stone bricks",			Material.SMOOTH_BRICK);
		tempMap.put("stone stair",			Material.SMOOTH_STAIRS);
		tempMap.put("stone stairs",			Material.SMOOTH_STAIRS);
		tempMap.put("potato",				Material.POTATO_ITEM);
		tempMap.put("carrot",				Material.CARROT_ITEM);
		tempMap.put("brewing stand",		Material.BREWING_STAND_ITEM);
		tempMap.put("cauldron",				Material.CAULDRON_ITEM);
		tempMap.put("carrot on stick",		Material.CARROT_STICK);
		tempMap.put("carrot on a stick",	Material.CARROT_STICK);
		tempMap.put("cobblestone wall",		Material.COBBLE_WALL);
		tempMap.put("acacia wood stairs",	Material.ACACIA_STAIRS);
		tempMap.put("dark oak wood stairs",	Material.DARK_OAK_STAIRS);
		tempMap.put("wood slab",			Material.WOOD_STEP);
		tempMap.put("double wood slab",		Material.WOOD_DOUBLE_STEP);
		tempMap.put("repeater",				Material.DIODE);
		tempMap.put("piston",				Material.PISTON_BASE);
		tempMap.put("sticky piston",		Material.PISTON_STICKY_BASE);
		tempMap.put("flower pot",			Material.FLOWER_POT_ITEM);
		tempMap.put("wood showel",			Material.WOOD_SPADE);
		tempMap.put("stone showel",			Material.STONE_SPADE);
		tempMap.put("gold showel",			Material.GOLD_SPADE);
		tempMap.put("iron showel",			Material.IRON_SPADE);
		tempMap.put("diamond showel",		Material.DIAMOND_SPADE);
		tempMap.put("steak",				Material.COOKED_BEEF);
		tempMap.put("cooked porkchop",		Material.GRILLED_PORK);
		tempMap.put("raw porkchop",			Material.PORK);
		tempMap.put("hardened clay",		Material.HARD_CLAY);
		tempMap.put("huge brown mushroom",	Material.HUGE_MUSHROOM_1);
		tempMap.put("huge red mushroom",	Material.HUGE_MUSHROOM_2);
		tempMap.put("mycelium",				Material.MYCEL);
		tempMap.put("poppy",				Material.RED_ROSE);
		tempMap.put("comparator",			Material.REDSTONE_COMPARATOR);
		tempMap.put("skull",				Material.SKULL_ITEM);
		tempMap.put("head",					Material.SKULL_ITEM);
		tempMap.put("redstone torch",		Material.REDSTONE_TORCH_ON);
		tempMap.put("redstone lamp",		Material.REDSTONE_LAMP_OFF);
		tempMap.put("glistering melon",		Material.SPECKLED_MELON);
//		tempMap.put("acacia leaves", 		Material.LEAVES_2); It wouldn't work with 1.6 :/
//		tempMap.put("acacia log", 			Material.LOG_2);
		tempMap.put("gunpowder",			Material.SULPHUR);
		tempMap.put("lilypad",				Material.WATER_LILY);
		tempMap.put("command block",		Material.COMMAND);
		
		for (Entry<String, Material> tempEntry : tempMap.entrySet()) {
			materialMap.put(StringUtils.stripChars(tempEntry.getKey(), " _-").toLowerCase(), tempEntry.getValue());
		}
	}
	
	public static String getBukkitVersion() {
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		return packageName.substring(packageName.lastIndexOf('.') + 1);
	}

	public static String colorizeName(String input) {
		if (input == null) return null;
		
		if (input.isEmpty()) return input;
		
		if (input.charAt(0) != ChatColor.COLOR_CHAR) {
			return ChestCommands.getSettings().default_color__name + addColors(input);
		} else {
			return addColors(input);
		}
	}

	public static List<String> colorizeLore(List<String> input) {
		if (input == null) return null;
		for (int i = 0; i < input.size(); i++) {
			
			String line = input.get(i);
			
			if (line.isEmpty()) continue;
			
			if (line.charAt(0) != ChatColor.COLOR_CHAR) {
				input.set(i, ChestCommands.getSettings().default_color__lore + addColors(line));
			} else {
				input.set(i, addColors(line));
			}
		}
		return input;
	}
	
	public static String addColors(String input) {
		if (input == null) return null;
		return ChatColor.translateAlternateColorCodes('&', input);
	}
	
	public static List<String> addColors(List<String> input) {
		if (input == null) return null;
		for (int i = 0; i < input.size(); i++) {
			input.set(i, addColors(input.get(i)));
		}
		return input;
	}
	
	public static String addYamlExtension(String input) {
		if (input == null) return null;
		return input.toLowerCase().endsWith(".yml") ? input : input + ".yml";
	}
	
	private static DecimalFormat decimalFormat = new DecimalFormat("0.##");
	public static String decimalFormat(double number) {
		return decimalFormat.format(number);
	}
	
	@SuppressWarnings("deprecation")
	public static Material matchMaterial(String input) {
		if (input == null) return null;
		
		input = StringUtils.stripChars(input.toLowerCase(), " _-");
		
		if (isValidInteger(input)) {
			return Material.getMaterial(Integer.parseInt(input));
		}
		
		return materialMap.get(input);
	}
	
	public static Sound matchSound(String input) {
		if (input == null) return null;
		
		input = StringUtils.stripChars(input.toLowerCase(), " _-");

		for (Sound sound : Sound.values()) {
			if (StringUtils.stripChars(sound.toString().toLowerCase(), "_").equals(input)) return sound;
		}
		return null;
	}
	
	public static String formatMaterial(Material material) {
		return StringUtils.capitalizeFully(material.toString().replace("_", " "));
	}
	
	public static int makePositive(int i) {
		return i < 0 ? 0 : i;
	}
	
	public static boolean isValidInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
	
	public static boolean isValidShort(String input) {
		try {
			Short.parseShort(input);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
	
	public static boolean isValidPositiveDouble(String input) {
		try {
			return Double.parseDouble(input) > 0.0;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
	
	public static List<String> readLines(File file) throws IOException, Exception {
		BufferedReader br = null;

		try {
			List<String> lines = Lists.newArrayList();

			if (!file.exists()) {
				throw new FileNotFoundException();
			}

			br = new BufferedReader(new FileReader(file));
			String line = br.readLine();

			while (line != null) {
				lines.add(line);
				line = br.readLine();
			}

			return lines;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
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
	
	public static void saveResourceSafe(Plugin plugin, String name) {
		try {
			plugin.saveResource(name, false);
		} catch (Exception ex) {
			// Shhh...
		}
	}
}
