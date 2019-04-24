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
package com.gmail.filoghost.chestcommands.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.chestcommands.ChestCommands;
import com.gmail.filoghost.chestcommands.exception.FormatException;
import com.gmail.filoghost.chestcommands.internal.ExtendedIconMenu;
import com.gmail.filoghost.chestcommands.internal.MenuInventoryHolder;

public class Utils {
	
	private static String nmsVersion;
	private static DecimalFormat decimalFormat = new DecimalFormat("0.##");
	
	
	public static String getNMSVersion() {
		if (nmsVersion == null) {
			String packageName = Bukkit.getServer().getClass().getPackage().getName();
			nmsVersion = packageName.substring(packageName.lastIndexOf('.') + 1);
		}
		
		return nmsVersion;
	}

	public static String colorizeName(String input) {
		if (input == null || input.isEmpty()) return input;

		if (input.charAt(0) != ChatColor.COLOR_CHAR) {
			return ChestCommands.getSettings().default_color__name + addColors(input);
		} else {
			return addColors(input);
		}
	}

	public static List<String> colorizeLore(List<String> input) {
		if (input == null || input.isEmpty()) return input;
		
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
	
	public static void refreshMenu(Player player) {
		InventoryView view = player.getOpenInventory();
		if (view != null) {
			Inventory topInventory = view.getTopInventory();
			if (topInventory.getHolder() instanceof MenuInventoryHolder) {
				MenuInventoryHolder menuHolder = (MenuInventoryHolder) topInventory.getHolder();
				
				if (menuHolder.getIconMenu() instanceof ExtendedIconMenu) {
					((ExtendedIconMenu) menuHolder.getIconMenu()).refresh(player, topInventory);
				}
			}
		}
	}
	
	public static String addColors(String input) {
		if (input == null || input.isEmpty()) return input;
		return ChatColor.translateAlternateColorCodes('&', input);
	}
	
	public static List<String> addColors(List<String> input) {
		if (input == null || input.isEmpty()) return input;
		for (int i = 0; i < input.size(); i++) {
			input.set(i, addColors(input.get(i)));
		}
		return input;
	}
	
	public static String addYamlExtension(String input) {
		if (input == null) return null;
		return input.toLowerCase().endsWith(".yml") ? input : input + ".yml";
	}
	
	public static String decimalFormat(double number) {
		return decimalFormat.format(number);
	}
	
	public static Sound matchSound(String input) {
		if (input == null) return null;
		
		input = StringUtils.stripChars(input.toLowerCase(), " _-");

		for (Sound sound : Sound.values()) {
			if (StringUtils.stripChars(sound.toString().toLowerCase(), "_").equals(input)) return sound;
		}
		return null;
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
	
	public static boolean isValidPositiveInteger(String input) {
		try {
			return Integer.parseInt(input) > 0;
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
			List<String> lines = newArrayList();

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
			// Ignore
		}
	}
	
	public static <T> Set<T> newHashSet() {
		return new HashSet<T>();
	}
	
	public static <T, V> Map<T, V> newHashMap() {
		return new HashMap<T, V>();
	}
	
	public static <T> List<T> newArrayList() {
		return new ArrayList<T>();
	}
	
	public static String join(Iterable<?> iterable, String separator) {
		StringBuilder builder = new StringBuilder();
		Iterator<?> iter = iterable.iterator();
		
		boolean first = true;
		
		while (iter.hasNext()) {
			if (first) {
				first = false;
			} else {
				builder.append(separator);
			}
			
			builder.append(iter.next());
		}
		
		return builder.toString();
	}
	
	public static boolean isClassLoaded(String name) {
		try {
			Class.forName(name);
			return true;
		} catch (Throwable t) {
			return false;
		}
	}

	public static boolean isNullOrEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}
	
}
